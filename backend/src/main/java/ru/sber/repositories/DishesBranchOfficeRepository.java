package ru.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.entities.BranchOffice;
import ru.sber.entities.DishesBranchOffice;

import java.util.List;

/**
 * Репозиторий для связи {@link ru.sber.entities.Dish блюд } и  {@link BranchOffice филиалов}
 */
@Repository
public interface DishesBranchOfficeRepository extends JpaRepository<DishesBranchOffice, Long> {
    /**
     * Проверяет есть ли блюдо в филиале
     *
     * @param branchId id филиала
     * @param dishId   id блюда
     * @return boolean
     */
    boolean existsByBranchOffice_IdAndDish_Id(Long branchId, Long dishId);

    /**
     * Проверяет есть ли блюдо в филиале по id
     *
     * @param dishId id блюда
     * @return boolean
     */
    boolean existsByDish_Id(Long dishId);

    /**
     * Выводит блюда по id филиала
     *
     * @param id id филиала
     * @return List<DishesBranchOffice>
     */
    List<DishesBranchOffice> findByBranchOffice_Id(long id);

    /**
     * Выводит блюдо по городу
     *
     * @param name имя города
     * @return List<DishesBranchOffice>
     */
    List<DishesBranchOffice> findByBranchOffice_NameCity(String name);

    /**
     * Удаляет блюдо по id в филиале
     *
     * @param id id блюда
     */
    void deleteByDish_Id(long id);
}
