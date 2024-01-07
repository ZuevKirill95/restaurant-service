package ru.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.entities.BranchOffice;
import ru.sber.entities.Notify;

/**
 * Репозиторий с {@link BranchOffice филиалами}
 */
@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    /**
     * Проверяет есть ли заказ в уведомлениях по id
     *
     * @param id id заказа
     * @return boolean
     */
    boolean existsByIdOrder(Long id);
}
