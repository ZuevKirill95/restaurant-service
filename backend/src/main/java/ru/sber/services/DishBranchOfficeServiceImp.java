package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sber.entities.DishesBranchOffice;
import ru.sber.exceptions.UserNotFound;
import ru.sber.repositories.DishesBranchOfficeRepository;
import ru.sber.repositories.UserRepository;

import java.util.List;

@Service
@Slf4j
public class DishBranchOfficeServiceImp implements DishBranchOfficeService {
    private final DishesBranchOfficeRepository dishesBranchOfficeRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public DishBranchOfficeServiceImp(DishesBranchOfficeRepository dishesBranchOfficeRepository,
                                      UserRepository userRepository,
                                      JwtService jwtService) {
        this.dishesBranchOfficeRepository = dishesBranchOfficeRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<DishesBranchOffice> getListDishBranchOffice() {
        var user = userRepository.findById(jwtService.getSubClaim(getUserJwtTokenSecurityContext()))
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));

        return dishesBranchOfficeRepository.findByBranchOffice_Id(user.getBranchOffice().getId());
    }

    @Override
    public List<DishesBranchOffice> getListDishBranchOfficeAll() {
        return dishesBranchOfficeRepository.findAll();
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
