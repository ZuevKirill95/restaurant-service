package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.entities.User;
import ru.sber.entities.enums.EStatusEmployee;
import ru.sber.exceptions.UserNotFound;
import ru.sber.repositories.BranchOfficeRepository;
import ru.sber.repositories.UserRepository;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final BranchOfficeRepository branchOfficeRepository;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImp(UserRepository userRepository, BranchOfficeRepository branchOfficeRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.branchOfficeRepository = branchOfficeRepository;
        this.jwtService = jwtService;
    }

    @Override
    public boolean addUserById(String userId, String idBranchOffice) {
        log.info("Регистрация сотрудника");

        var user = new User(userId);
        var branchOffice = branchOfficeRepository.findById(Long.parseLong(idBranchOffice))
                .orElseThrow(() -> new RuntimeException("Филиал не найден"));

        user.setBranchOffice(branchOffice);
        user.setStatus(EStatusEmployee.INACTIVE);
        userRepository.save(user);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteById() {
        log.info("Удаляет сотрудника");
        var user = userRepository.findById(jwtService.getSubClaim(getUserJwtTokenSecurityContext()))
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));

        return userRepository.deleteById(user.getId());
    }

    @Override
    public User findById() {
        return userRepository.findById(jwtService.getSubClaim(getUserJwtTokenSecurityContext()))
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));
    }

    @Override
    public String userUpdate(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public String getUserToken(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));
        return user.getResetPasswordToken();
    }

    @Override
    public String deleteTokenById(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));
        user.setResetPasswordToken("");

        return userRepository.save(user).getId();
    }

    private Jwt getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            return jwtAuthenticationToken.getToken();
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }
}
