package ru.sber.services;

import org.springframework.http.ResponseEntity;

/**
 * Сервис для аналитических запросов
 */
public interface AnalyticService {
    /**
     * Определяет количество заказов принятых работником ресторана
     *
     * @return количество заказов
     */
    ResponseEntity<?> findCountOrderFromEmployeeRestaurantId();

    /**
     * Определяет количество заказов поступивших за месяц
     *
     * @param year  год
     * @param mouth месяц
     * @return количество заказов
     */
    ResponseEntity<?> findOrdersPerMonth(Integer year, Integer mouth);
}
