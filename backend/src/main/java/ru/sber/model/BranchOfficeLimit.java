package ru.sber.model;

import lombok.Data;
import ru.sber.entities.BranchOffice;
import ru.sber.entities.User;

/**
 * Обрезанный {@link User сотрудник} для вывода информации из БД
 */
@Data
public class BranchOfficeLimit {
    private String address;
    private String status;
    private String nameCity;

    public BranchOfficeLimit(BranchOffice branchOffice) {
        this.address = branchOffice.getAddress();
        this.status = branchOffice.getStatus();
        this.nameCity = branchOffice.getNameCity();
    }
}