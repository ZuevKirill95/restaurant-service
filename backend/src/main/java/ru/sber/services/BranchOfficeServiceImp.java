package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sber.entities.enums.EStatusEmployee;
import ru.sber.exceptions.UserNotFound;
import ru.sber.model.BranchOfficeLimit;
import ru.sber.repositories.BranchOfficeRepository;
import ru.sber.repositories.UserRepository;

import java.util.List;

@Slf4j
@Service
public class BranchOfficeServiceImp implements BranchOfficeService {
    private final BranchOfficeRepository branchOfficeRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public BranchOfficeServiceImp(BranchOfficeRepository branchOfficeRepository, JwtService jwtService, UserRepository userRepository) {
        this.branchOfficeRepository = branchOfficeRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean openCloseBranchOffice() {
        log.info("Закрывает/Открывает филиал");

        var user = userRepository.findById(jwtService.getSubClaim(getUserJwtTokenSecurityContext()))
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));
        var isExit = userRepository.countByBranchOffice_IdAndStatus(
                user.getBranchOffice().getId(), EStatusEmployee.INACTIVE);

        if (isExit == 1) {
            var branchOffice = user.getBranchOffice();
            branchOffice.setStatus(branchOffice.getStatus().equals("OPEN") ? "CLOSE" : "OPEN");
            branchOfficeRepository.save(branchOffice);
            return true;
        }
        return false;
    }

    @Override
    public BranchOfficeLimit getBranchOfficeByEmployee() {
        log.info("Получает информацию о филиале по сотруднику");

        var user = userRepository.findById(jwtService.getSubClaim(getUserJwtTokenSecurityContext()))
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));

        return new BranchOfficeLimit(user.getBranchOffice());
    }

    @Override
    public List<BranchOfficeLimit> getListBranchOffice() {
        log.info("Получает информацию о всех филиалах");

        return branchOfficeRepository.findAll().stream().map(BranchOfficeLimit::new).toList();
    }

    /**
     * Получение данных о пользователе
     */
    private Jwt getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            return jwtAuthenticationToken.getToken();
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }
}
