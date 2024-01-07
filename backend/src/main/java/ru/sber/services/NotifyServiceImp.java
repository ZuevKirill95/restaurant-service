package ru.sber.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sber.entities.Notify;
import ru.sber.entities.User;
import ru.sber.entities.enums.EStatusEmployee;
import ru.sber.exceptions.UserNotApproved;
import ru.sber.exceptions.UserNotFound;
import ru.sber.repositories.NotifyRepository;
import ru.sber.repositories.UserRepository;

import java.util.List;

@Slf4j
@Service
public class NotifyServiceImp implements NotifyService {
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public NotifyServiceImp(NotifyRepository notifyRepository, UserRepository userRepository, JwtService jwtService) {
        this.notifyRepository = notifyRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public long addNotify(Long id) {
        var isNotify = notifyRepository.existsByIdOrder(id);
        log.info("Добавляет в активные уведомления false={}", isNotify);

        if (isNotify) {
            return id;
        } else {
            return notifyRepository.save(new Notify(id)).getId();
        }
    }

    @Override
    public String getList() {
        var user = getUserJwtTokenSecurityContext();
        if (user.getStatus().equals(EStatusEmployee.UNDER_CONSIDERATION)){
            throw new UserNotApproved("Пользователь не допущен к работе");
        }

        List<Notify> listOrder = notifyRepository.findAll();

        if (!listOrder.isEmpty()) {
            notifyRepository.deleteAll();
            return listOrder.stream()
                    .map(item -> item.getIdOrder().toString())
                    .reduce("", (s1, s2) -> s1.isEmpty() ? s2 : s1 + "," + s2);
        }

        return "";
    }

    private User getUserJwtTokenSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

            var user = userRepository.findById(jwtService.getSubClaim(jwtAuthenticationToken.getToken()))
                    .orElseThrow(() -> new UserNotFound("Пользователь не найден"));

            return user;
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
    }
}
