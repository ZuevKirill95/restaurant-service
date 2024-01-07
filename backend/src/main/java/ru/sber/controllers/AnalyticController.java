package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.services.AnalyticService;

/**
 * Контроллер для взаимодействия с аналитикой сервиса
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("analytic")
public class AnalyticController {
    private final AnalyticService analyticService;

    @Autowired
    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("/employee")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> getCountOrderFromEmployee() {
        log.info("Получает количество заказов сделанных работником ресторана");

        return analyticService.findCountOrderFromEmployeeRestaurantId();
    }

    @GetMapping("/orders/per/month")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<?> getOrderPerData(@RequestParam(required = false) Integer year,
                                             @RequestParam(required = false) Integer month) {
        log.info("Получает количество заказов поступивших за месяц");

        return analyticService.findOrdersPerMonth(year, month);
    }
}
