package ru.sber.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Заказ
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private Long clientId;
    private String clientName;
    private String description;
    private String clientPhone;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime orderCookingTime;
    private LocalDateTime orderCookedTime;
    private String address;
    private String branchAddress;
    private Long branchId;
    private String employeeRestaurantId;
    private List<?> dishesOrders;
}