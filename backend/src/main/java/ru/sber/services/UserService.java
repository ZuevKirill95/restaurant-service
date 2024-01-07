package ru.sber.services;

import ru.sber.entities.User;

/**
 * Сервис для взаимодействия с {@link User сотрудником}
 */
public interface UserService {

    /**
     * Сохраняет данные сотрудника
     *
     * @param userId id сотрудник
     */
    boolean addUserById(String userId, String idBranchOffice);

    /**
     * Удаляет сотрудника по id
     *
     * @return boolean
     */
    boolean deleteById();

    User findById();
    User findById(String userId);
    String userUpdate(User user);
    String getUserToken(String userId);
    String deleteTokenById(String userId);
}
