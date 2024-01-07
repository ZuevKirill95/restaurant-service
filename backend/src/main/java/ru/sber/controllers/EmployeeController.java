package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.entities.User;
import ru.sber.services.UserService;

/**
 * Контроллер для взаимодействия {@link User сотрудником}
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("employees")
public class EmployeeController {

    private final UserService userService;

    @Autowired
    public EmployeeController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmployeeById() {
        log.info("Удаление аккаунта сотрудника");

        var isDeleted = userService.deleteById();

        if (isDeleted) {
            return ResponseEntity.noContent()
                    .build();
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

}