package ru.sber.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.sber.model.Order;
import ru.sber.services.NotifyServiceImp;

/**
 * Класс для получения информации из кафки
 */
@Slf4j
@Component
public class KafkaListeners {
    private final NotifyServiceImp notifyServiceImp;

    public KafkaListeners(NotifyServiceImp notifyServiceImp) {
        this.notifyServiceImp = notifyServiceImp;
    }

    @KafkaListener(topics = "review_order", groupId = "selfMessages")
    void listenerReviewOrder(String data) {
        log.info("Listener получил: {}", data);
    }

    @KafkaListener(topics = "restaurant_status", groupId = "selfMessages")
    void listenerRestaurantStatus(String data) throws JsonProcessingException {
        log.info("Listener получил: {}", data);

        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(data, Order.class);
        log.info("Order: {}", order);
        notifyServiceImp.addNotify(order.getId());
    }
}
