package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.entities.Dish;
import ru.sber.services.DishService;

import java.net.URI;
import java.util.List;

/**
 * Контроллер для взаимодействия {@link Dish блюдами}
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("dishes")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Long> addDish(@RequestBody Dish dish) {
        log.info("Добавляет блюдо с именем {}", dish.getName());

        return ResponseEntity.created(URI.create("dishes/" + dishService.addDish(dish)))
                .build();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Long> addDishByName(@RequestBody Dish dish) {
        log.info("Добавляет блюдо по имени");

        var isAdd = dishService.addDishByName(dish.getName());

        if (isAdd) {
            return ResponseEntity.accepted()
                    .build();
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> updateDish(@RequestBody Dish dish) {
        log.info("Обновляет блюдо по id {}", dish.getId());

        var isUpdate = dishService.updateDish(dish);

        if (isUpdate) {
            return ResponseEntity.accepted()
                    .build();
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dish>> getListDish() {
        log.info("Получает блюда в филиале");

        return ResponseEntity.ok()
                .body(dishService.getListDish());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable long id) {
        log.info("Получает блюдо по id");

        return dishService.getDishById(id).map(
                        value -> ResponseEntity.ok()
                                .body(value))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/customer/any")
    public ResponseEntity<Page<Dish>> getListAllDish(@RequestParam int page, @RequestParam int size) {
        log.info("Получает все блюда");

        return ResponseEntity.ok()
                .body(dishService.getDishesByPage(page, size));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Dish>> getListAllDish(@RequestParam String city) {
        log.info("Получает все блюда в городе");

        return ResponseEntity.ok()
                .body(dishService.getListByNameCity(city));
    }

    @GetMapping("/customer/basket")
    public ResponseEntity<List<Dish>> getListDishesById(@RequestParam String list) {
        log.info("Получает список блюд по id {}", list);

        return ResponseEntity.ok()
                .body(dishService.getListById(list));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Dish> deleteDishById(@PathVariable long id) {
        log.info("Удаляет из филиала блюдо по id");

        var isDeleted = dishService.deleteDish(id);

        if (isDeleted) {
            return ResponseEntity.noContent()
                    .build();
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }
}