package ru.sber.services;

import ru.sber.entities.BranchOffice;
import ru.sber.model.BranchOfficeLimit;

import java.util.List;

/**
 * Сервис для взаимодействия с {@link BranchOffice филиалом}
 */
public interface BranchOfficeService {
    /**
     * Закрывает филиал
     *
     * @return boolean
     */
    boolean openCloseBranchOffice();

    /**
     * Получает информацию о филиале
     *
     * @return BranchOffice
     */
    BranchOfficeLimit getBranchOfficeByEmployee();

    /**
     * Получает информацию о филиалах
     *
     * @return List<BranchOffice>
     */
    List<BranchOfficeLimit> getListBranchOffice();
}
