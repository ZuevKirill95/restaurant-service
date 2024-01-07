package ru.sber.services;

import org.springframework.http.ResponseEntity;
import ru.sber.model.Order;

import java.util.List;

/**
 * Получает информацию о заказе
 */
public interface OrderService {
    /**
     * Обновляет статус заказа
     *
     * @param id     id заказа
     * @param order заказ
     */
    ResponseEntity<?> updateOrderStatusById(Long id, Order order);

    /**
     * Отменяет заказ
     *
     * @param id      id заказа
     * @param massage сообщение
     */
    ResponseEntity<?> cancelOrderById(Long id, Object massage);

    /**
     * Отменяет заказы
     *
     * @param listId listId заказов
     */
    ResponseEntity<?> cancelOrderByListId(String listId, Object massage);

    /**
     * Получает информацию о заказах
     *
     * @return List<?>
     */
    List<?> getListOrders();

    /**
     * Получает информацию о заказах после уведомления
     *
     * @return List<?>
     */
    List<?> getListOrdersByNotify();
}
