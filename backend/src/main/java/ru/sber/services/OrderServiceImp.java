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
import ru.sber.entities.User;
import ru.sber.entities.enums.EStatusEmployee;
import ru.sber.exceptions.UserNotApproved;
import ru.sber.exceptions.UserNotFound;
import ru.sber.model.Order;
import ru.sber.order.OrderFeign;
import ru.sber.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализует логику работы с order-service
 */
@Slf4j
@Service
public class OrderServiceImp implements OrderService {

    private final OrderFeign orderFeign;
    private final NotifyService notifyService;
    private final OrderTokenService orderTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImp(OrderFeign orderFeign,
                           NotifyService notifyService,
                           OrderTokenService orderTokenService,
                           JwtService jwtService,
                           UserRepository userRepository) {
        this.orderFeign = orderFeign;
        this.notifyService = notifyService;
        this.orderTokenService = orderTokenService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> updateOrderStatusById(Long id, Order order) {
        checkAndUpdateOrderTokens();
        Optional<OrderToken> orderToken = orderTokenService.findById();
        var user = getUserJwtTokenSecurityContext();

        order.setBranchAddress(user.getBranchOffice().getAddress());
        order.setBranchId(user.getBranchOffice().getId());
        order.setEmployeeRestaurantId(user.getId());

        return orderFeign.updateOrderStatusById("Bearer "+ orderToken.get().getAccessToken(), id, order);
    }

    @Override
    public ResponseEntity<?> cancelOrderById(Long id, Object massage) {
        checkAndUpdateOrderTokens();
        Optional<OrderToken> orderToken = orderTokenService.findById();

        return orderFeign.cancelOrderById("Bearer "+ orderToken.get().getAccessToken(), id, massage);
    }

    @Override
    public ResponseEntity<?> cancelOrderByListId(String listId, Object massage) {
        checkAndUpdateOrderTokens();
        Optional<OrderToken> orderToken = orderTokenService.findById();

        return orderFeign.cancelOrderByListId("Bearer "+ orderToken.get().getAccessToken(), listId, massage);
    }

    @Override
    public List<?> getListOrders() {
        log.info("Получает заказы для ресторана");
        var user = getUserJwtTokenSecurityContext();
        if (user.getStatus().equals(EStatusEmployee.UNDER_CONSIDERATION)){
            throw new UserNotApproved("Пользователь не допущен к работе");
        }
        checkAndUpdateOrderTokens();
        Optional<OrderToken> orderToken = orderTokenService.findById();

        return orderFeign.getListOrders("Bearer "+ orderToken.get().getAccessToken()).getBody();
    }

    @Override
    public List<?> getListOrdersByNotify() {
        String strOrder = notifyService.getList();
        log.info("Обновляет информацию о заказах с id {}", strOrder);

        checkAndUpdateOrderTokens();
        Optional<OrderToken> orderToken = orderTokenService.findById();

        if (!strOrder.isEmpty()) {
                return orderFeign.getListOrdersByNotify(
                        "Bearer "+ orderToken.get().getAccessToken(), strOrder);
        }
        return List.of();
    }

    private User getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            return userRepository.findById(jwtService.getSubClaim(jwtAuthenticationToken.getToken()))
                    .orElseThrow(() -> new UserNotFound("Пользователь не найден"));
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }

    private void checkAndUpdateOrderTokens() {
        Optional<OrderToken> orderTokens = orderTokenService.findById();
        log.info("Проверка получения токена");

        if (orderTokens.isEmpty() ||
                !orderTokens.stream().allMatch(token -> LocalDateTime.now().isBefore(token.getTokenExpiration()))) {
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
