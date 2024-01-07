package ru.sber.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sber.entities.BranchOffice;
import ru.sber.model.BranchOfficeLimit;
import ru.sber.services.BranchOfficeService;

import java.util.List;

/**
 * Контроллер для взаимодействия {@link BranchOffice филиалами}
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("branch-offices")
public class BranchOfficeController {
    private final BranchOfficeService branchOfficeService;

    @Autowired
    public BranchOfficeController(BranchOfficeService branchOfficeService) {
        this.branchOfficeService = branchOfficeService;
    }

    @PutMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> openCloseBranchOffice() {
        log.info("Закрывает или открывает филиал");

        boolean isStatus = branchOfficeService.openCloseBranchOffice();

        if (isStatus) {
            return ResponseEntity.ok()
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied");
        }
    }

    @GetMapping
    public ResponseEntity<BranchOfficeLimit> getBranchOfficeByEmployee() {
        log.info("Выводит данные о филиале");

        return ResponseEntity.ok()
                .body(branchOfficeService.getBranchOfficeByEmployee());
    }

    @GetMapping("/all")
    public ResponseEntity<List<BranchOfficeLimit>> getListBranchOffice() {
        log.info("Выводит данные о всех филиалах");

        return ResponseEntity.ok()
                .body(branchOfficeService.getListBranchOffice());
    }
}
