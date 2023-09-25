package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.entities.BranchOffice;
import ru.sber.entities.Dish;
import ru.sber.entities.DishesBranchOffice;
import ru.sber.exceptions.NoFoundEmployeeException;
import ru.sber.repositories.DishRepository;
import ru.sber.repositories.DishesBranchOfficeRepository;
import ru.sber.security.services.EmployeeDetailsImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DishServiceImp implements DishService {
    private final DishRepository dishRepository;
    private final DishesBranchOfficeRepository dishesBranchOfficeRepository;

    public DishServiceImp(DishRepository dishRepository, DishesBranchOfficeRepository dishesBranchOfficeRepository) {
        this.dishRepository = dishRepository;
        this.dishesBranchOfficeRepository = dishesBranchOfficeRepository;
    }

    @Override
    public List<Dish> getListDish() {
        return dishesBranchOfficeRepository.findByBranchOffice_Id(getBranchOffice().getId())
                .stream()
                .map(DishesBranchOffice::getDish)
                .toList();
    }

    @Override
    public boolean addDish(Dish dish) {
        log.info("Добавляет блюдо с id {}", dish.getId());

        dishRepository.save(dish);
        dishesBranchOfficeRepository.save(new DishesBranchOffice(dish, getBranchOffice()));
        return true;
    }

    @Override
    @Transactional
    public boolean deleteDish(long id) {
        log.info("Удаляет блюдо с id {}", id);

        dishRepository.deleteById(id);
        return false;
    }

    @Override
    public boolean updateDish(Dish dish) {
        log.info("Обновляет блюдо с id {}", dish.getId());

        if (dishesBranchOfficeRepository.existsByBranchOffice_IdAndDish_Id(getBranchOffice().getId(), dish.getId())) {
            dishRepository.save(dish);
            return true;
        }

        return false;
    }

    @Override
    public Optional<Dish> getDishById(long id) {
        log.info("Получает блюдо с id {}", id);

        if (dishesBranchOfficeRepository.existsByBranchOffice_IdAndDish_Id(getBranchOffice().getId(), id)) {
            return dishRepository.findById(id);
        }

        return Optional.empty();
    }

    private BranchOffice getBranchOffice() {
        log.info("Получает id филиала");

        var employee = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (employee instanceof EmployeeDetailsImpl) {
            return ((EmployeeDetailsImpl) employee).getBranchOffice();
        } else {
            throw new NoFoundEmployeeException("Сотрудник не найден");
        }
    }
}