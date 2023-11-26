package ru.sber.services;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сервис для аналитических запросов
 */
public interface AnalyticService {
    /**
     * Определяет количество заказов принятых работником ресторана
     * @param employeeRestaurantId id работника ресторана
     * @return количество заказов
     */
    ResponseEntity<?> findCountOrderFromEmployeeRestaurantId(long employeeRestaurantId);

    /**
     * Определяет количество заказов поступивших за месяц
     * @param year год
     * @param mouth месяц
     * @return количество заказов
     */
    ResponseEntity<?> findOrdersPerMonth(Integer year, Integer mouth);
}
