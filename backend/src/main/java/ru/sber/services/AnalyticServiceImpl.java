package ru.sber.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.sber.entities.OrderToken;
import ru.sber.exceptions.UserNotFound;
import ru.sber.order.OrderFeign;
import ru.sber.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AnalyticServiceImpl implements AnalyticService {

    private final OrderFeign orderFeign;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OrderTokenService orderTokenService;

    @Autowired
    public AnalyticServiceImpl(OrderFeign orderFeign, JwtService jwtService, UserRepository userRepository, OrderTokenService orderTokenService) {
        this.orderFeign = orderFeign;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.orderTokenService = orderTokenService;
    }

    @Override
    public ResponseEntity<?> findCountOrderFromEmployeeRestaurantId() {
        var user = userRepository.findById(jwtService.getSubClaim(getUserJwtTokenSecurityContext()))
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));

        log.info("Ищет количество заказов у сотрудника с id {}", user.getId());

        checkAndUpdateOrderTokens();
        OrderToken orderToken = orderTokenService.findById().get();

        return orderFeign.getCountOrderFromEmployeeRestaurant("Bearer "+ orderToken.getAccessToken(), user.getId());
    }

    @Override
    public ResponseEntity<?> findOrdersPerMonth(Integer year, Integer mouth) {
        log.info("Ищет количество заказов за месяц начиная с год(а) {}, месяц(а) {}", year, mouth);

        checkAndUpdateOrderTokens();
        OrderToken orderToken = orderTokenService.findById().get();

        return orderFeign.getOrderPerMonth(
                "Bearer "+ orderToken.getAccessToken(),
                year,
                mouth);
    }

    private Jwt getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            return jwtAuthenticationToken.getToken();
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }

    private void checkAndUpdateOrderTokens() {
        Optional<OrderToken> orderTokens = orderTokenService.findById();
        log.info("Проверка получения токена");

        if (orderTokens.isEmpty() || !orderTokens.stream()
                .allMatch(token -> LocalDateTime.now().isBefore(token.getTokenExpiration()))) {
            updateOrderTokens();
        }
    }

    @Transactional
    private void updateOrderTokens() {
        log.info("Обновление токена заказа");

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "password");
        tokenBody.add("client_id", "login-app");
        tokenBody.add("username", "user");
        tokenBody.add("password", "11111");

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        try {
            ResponseEntity<String> tokenResponseEntity = new RestTemplate().exchange(
                    "http://localhost:8080/realms/order-realm/protocol/openid-connect/token",
                    HttpMethod.POST, tokenEntity, String.class);

            String jsonResponse = tokenResponseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            String accessToken = jsonNode.get("access_token").asText();

            OrderToken newToken = new OrderToken(1, accessToken, LocalDateTime.now().plusMinutes(14));

            orderTokenService.save(newToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
