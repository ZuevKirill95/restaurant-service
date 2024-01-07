package ru.sber.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.sber.config.GeneratePasswordCode;
import ru.sber.entities.BranchOffice;
import ru.sber.entities.User;
import ru.sber.entities.enums.EStatusEmployee;
import ru.sber.entities.request.LoginRequest;
import ru.sber.entities.request.SignupRequest;
import ru.sber.exceptions.UserInvalidToken;
import ru.sber.exceptions.UserNotFound;
import ru.sber.model.*;
import ru.sber.services.EmailService;
import ru.sber.services.JwtService;
import ru.sber.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для взаимодействия с регистрацией и входом у {@link User сотрудника}
 */
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final String keycloakTokenUrl = "http://localhost:8080/realms/restaurant-realm/protocol/openid-connect/token";
    private final String keycloakCreateUserUrl = "http://localhost:8080/admin/realms/restaurant-realm/users";
    private final String keycloakUpdateUserUrl = "http://localhost:8080/admin/realms/restaurant-realm/users/";
    private final String clientId = "login-app";
    private final String grantType = "password";
    private final UserService userService;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, JwtService jwtService, EmailService emailService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(@RequestBody SignupRequest signupRequest) throws JsonProcessingException {
        log.info("Регистрация пользователя с email {}", signupRequest.getEmail());

        RequestUser requestUser = new RequestUser();
        requestUser.setUsername(signupRequest.getUsername());
        requestUser.setEmail(signupRequest.getEmail());
        requestUser.setEnabled(true);
        requestUser.setFirstName(signupRequest.getFirstName());
        requestUser.setLastName(signupRequest.getLastName());

        Attributes attributes = new Attributes();
        attributes.setPhoneNumber(signupRequest.getPhoneNumber());
        requestUser.setAttributes(attributes);

        Credential credential = new Credential();
        credential.setType(grantType);
        credential.setValue(signupRequest.getPassword());

        List<Credential> credentials = new ArrayList<>();
        credentials.add(credential);
        requestUser.setCredentials(credentials);

        HttpHeaders userHeaders = getHttpHeadersAdmin();
        HttpEntity<RequestUser> userEntity = new HttpEntity<>(requestUser, userHeaders);

        try {
            ResponseEntity<String> userResponseEntity = new RestTemplate().exchange(
                    keycloakCreateUserUrl, HttpMethod.POST, userEntity, String.class);

            String responseHeader = userResponseEntity.getHeaders().get("Location").get(0);

            int lastSlashIndex = responseHeader.lastIndexOf("/");
            String userId = responseHeader.substring(lastSlashIndex + 1);

            userService.addUserById(userId, signupRequest.getIdBranchOffice());

            return new ResponseEntity<>(userResponseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signInUser(@RequestBody LoginRequest loginRequest) {
        log.info("Вход пользователя с Username: {}", loginRequest.getUsername());

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", grantType);
        tokenBody.add("client_id", clientId);
        tokenBody.add("username", loginRequest.getUsername());
        tokenBody.add("password", loginRequest.getPassword());

        return getStringResponseEntity(tokenHeaders, tokenBody);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshUser(@RequestBody RefreshToken refreshToken) {
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "refresh_token");
        tokenBody.add("client_id", clientId);
        tokenBody.add("refresh_token", refreshToken.getRefresh_token());

        return getStringResponseEntity(tokenHeaders, tokenBody);
    }

    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<UserDetails> getUserDetails() {
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);
        Jwt jwt = getUserJwtTokenSecurityContext();

        var user = userService.findById();
        if (!user.getStatus().name().equals(EStatusEmployee.UNDER_CONSIDERATION.name())) {
            user.setStatus(EStatusEmployee.ACTIVE);
            userService.userUpdate(user);
        }

        var userDetails = new UserDetails(
                jwtService.getPreferredUsernameClaim(jwt),
                jwtService.getEmailClaim(jwt),
                jwtService.getPhoneNumberClaim(jwt),
                user.getBranchOffice(),
                user.getStatus().name());

        return new ResponseEntity<>(userDetails, userHeaders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('client_user')")
    @PutMapping
    @Transactional
    public ResponseEntity<?> updateUserInfo(@RequestBody SignupRequest signupRequest) throws JsonProcessingException {
        log.info("Обновляет данные о клиенте c email {}", signupRequest.getEmail());
        Jwt jwt = getUserJwtTokenSecurityContext();

        UpdateUserData updateUserData = new UpdateUserData();
        updateUserData.setEmail(Optional.ofNullable(signupRequest.getEmail())
                .orElseGet(() -> jwtService.getEmailClaim(jwt))
        );
        updateUserData.setFirstName(Optional.ofNullable(signupRequest.getFirstName())
                .orElseGet(() -> jwtService.getFirstNameClaim(jwt))
        );
        updateUserData.setLastName(Optional.ofNullable(signupRequest.getLastName())
                .orElseGet(() -> jwtService.getLastNameClaim(jwt))
        );

        Attributes attributes = new Attributes();
        attributes.setPhoneNumber(Optional.ofNullable(signupRequest.getPhoneNumber())
                .orElseGet(() -> jwtService.getPhoneNumberClaim(jwt))
        );
        updateUserData.setAttributes(attributes);

        HttpHeaders userHeaders = getHttpHeadersAdmin();
        HttpEntity<UpdateUserData> userEntity = new HttpEntity<>(updateUserData, userHeaders);

        log.info("Http entity: {}", userEntity);
        try {
            ResponseEntity<String> userResponseEntity = new RestTemplate().exchange(
                    keycloakUpdateUserUrl + jwtService.getSubClaim(getUserJwtTokenSecurityContext()),
                    HttpMethod.PUT, userEntity, String.class);
            log.info("Результат отправки на keycloak: {}", userResponseEntity.getStatusCode());

            var user = userService.findById();
            user.setBranchOffice(signupRequest.getIdBranchOffice() != null ?
                    new BranchOffice(Long.parseLong(signupRequest.getIdBranchOffice())) : user.getBranchOffice());
            userService.userUpdate(user);

            return new ResponseEntity<>(userResponseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/logout")
    public ResponseEntity<String> logOutUser() {
        log.info("Выход пользователя");

        var user = userService.findById();
        if (!user.getStatus().name().equals(EStatusEmployee.UNDER_CONSIDERATION.name())) {
            user.setStatus(EStatusEmployee.INACTIVE);
            userService.userUpdate(user);
        }

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/reset-password/token")
    public ResponseEntity<?> sendPasswordToken(@RequestBody ResetPassword resetPassword) throws JsonProcessingException {
        log.info("Получение токена для изменения пароля у пользователя {}", resetPassword.getEmail());

        RequestResetPassword requestResetPassword = new RequestResetPassword();
        requestResetPassword.setType("password");
        requestResetPassword.setTemporary(false);
        String newPassword = resetPassword.getPassword();
        requestResetPassword.setValue(newPassword);

        HttpHeaders headersAdmin = getHttpHeadersAdmin();
        HttpEntity<RequestResetPassword> resetEntity = new HttpEntity<>(requestResetPassword, headersAdmin);

        try {
            ResponseEntity<String> userResponseEntity = new RestTemplate().exchange(
                    keycloakCreateUserUrl + "?email=" + resetPassword.getEmail(),
                    HttpMethod.GET, resetEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode usersNode = objectMapper.readTree(userResponseEntity.getBody());

            if (!usersNode.isArray() || usersNode.size() <= 0) {
                throw new UserNotFound("Пользователь не найден");
            }

            JsonNode userNode = usersNode.get(0);
            var user = userService.findById(userNode.get("id").asText());
            String passwordToken = GeneratePasswordCode.generateOneTimeToken();
            user.setResetPasswordToken(passwordToken);
            userService.userUpdate(user);
            resetPassword.setToken(passwordToken);

            emailService.sendResetPasswordToken(resetPassword);
            return new ResponseEntity<>(userResponseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPassword resetPassword) throws JsonProcessingException {
        log.info("Обновление пароля у пользователя {}", resetPassword.getEmail());

        RequestResetPassword requestResetPassword = new RequestResetPassword();
        requestResetPassword.setType("password");
        requestResetPassword.setTemporary(false);
        String newPassword = resetPassword.getPassword();
        requestResetPassword.setValue(newPassword);

        HttpHeaders userHeaders = getHttpHeadersAdmin();
        HttpEntity<RequestResetPassword> resetEntity = new HttpEntity<>(requestResetPassword, userHeaders);

        log.info("Http entity: {}", resetEntity);
        try {
            ResponseEntity<String> userResponseEntity = new RestTemplate().exchange(
                    keycloakCreateUserUrl + "?email=" + resetPassword.getEmail(),
                    HttpMethod.GET, resetEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode usersNode = objectMapper.readTree(userResponseEntity.getBody());
            if (!usersNode.isArray() || usersNode.size() <= 0) {
                throw new UserNotFound("Пользователь не найден");
            }
            JsonNode userNode = usersNode.get(0);
            String userId = userNode.get("id").asText();

            if (!resetPassword.getToken().trim().equals(userService.getUserToken(userId))){
                throw new UserInvalidToken("Неверный токен");
            } else {
                userService.deleteTokenById(userId);
            }

            ResponseEntity<String> resetResponseEntity = new RestTemplate().exchange(
                    keycloakUpdateUserUrl + userId + "/reset-password",HttpMethod.PUT, resetEntity, String.class);
            log.info("Результат отправки на keycloak: {}", resetResponseEntity.getStatusCode());

            resetPassword.setPassword(newPassword);
            emailService.sendUpdatePasswordToken(resetPassword);
            return new ResponseEntity<>(resetResponseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> getStringResponseEntity(HttpHeaders tokenHeaders, MultiValueMap<String, String> tokenBody) {
        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        try {
            ResponseEntity<String> tokenResponseEntity = new RestTemplate().exchange(
                    keycloakTokenUrl, HttpMethod.POST, tokenEntity, String.class);

            return new ResponseEntity<>(tokenResponseEntity.getBody(),
                    tokenResponseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Получение токена из контекста
     */
    private Jwt getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            return jwtAuthenticationToken.getToken();
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }

    private HttpHeaders getHttpHeadersAdmin() throws JsonProcessingException {
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(signInUser(new LoginRequest("admin", "11111")).getBody());
        String accessToken = rootNode.path("access_token").asText();
        userHeaders.setBearerAuth(accessToken);
        return userHeaders;
    }
}