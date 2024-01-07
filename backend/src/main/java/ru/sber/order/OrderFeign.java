package ru.sber.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sber.model.Order;

import java.util.List;

/**
 * Отвечает за взаимодействие с order-service
 */
@FeignClient(name = "orderService", url = "localhost:8083/")
public interface OrderFeign {
    @PutMapping("orders/{id}")
    ResponseEntity<?> updateOrderStatusById(@RequestHeader("Authorization") String bearerToken,
                                            @PathVariable Long id,
                                            @RequestBody Order order);

    @PutMapping("orders/{id}/cancel")
    ResponseEntity<?> cancelOrderById(@RequestHeader("Authorization") String bearerToken,
                                      @PathVariable Long id,
                                      @RequestBody Object massage);

    @PutMapping("orders/cancel")
    ResponseEntity<?> cancelOrderByListId(@RequestHeader("Authorization") String bearerToken,
                                          @RequestParam String listId,
                                          @RequestBody Object message);

    @GetMapping("orders")
    ResponseEntity<List<?>> getListOrders(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("orders/orders/notify/{orderId}")
    List<?> getListOrdersByNotify(@RequestHeader("Authorization") String bearerToken,
                                  @PathVariable String orderId);

    @GetMapping("analytic/employee/{id}")
    ResponseEntity<Integer> getCountOrderFromEmployeeRestaurant(@RequestHeader("Authorization") String bearerToken,
                                                                @PathVariable("id") String idEmployeeRestaurant);

    @GetMapping("analytic/orders/per/month")
    ResponseEntity<Long> getOrderPerMonth(@RequestHeader("Authorization") String bearerToken,
                                          @RequestParam(required = false) Integer year,
                                          @RequestParam(required = false) Integer month);
}