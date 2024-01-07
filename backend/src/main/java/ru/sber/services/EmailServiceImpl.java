package ru.sber.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.sber.config.GeneratePasswordCode;
import ru.sber.model.ResetPassword;

@Slf4j
@Service
@Data
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final String emailAddress = "mish.uxin2012@yandex.ru";

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendResetPasswordToken(ResetPassword resetPassword) throws MessagingException {
        log.info("Отправляем токен для сброса пароля: {}", resetPassword.getEmail());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailAddress);

        helper.setTo(resetPassword.getEmail());
        helper.setSubject("Привет!");
        helper.setText(String.format("""
                Мы получили запрос на сброса пароля для твоего аккаунта. Чтобы восстановить пароль, введи следующий токен:
                
                Токен для сброса пароля: %s
                
                Если ты не запрашивал восстановление пароля, проигнорируй это сообщение. Если сам запросил восстановление, пожалуйста, следуй инструкциям далее.
                                             
                С уважением,
                Команда поддержки сайта
                """, resetPassword.getToken()));
        javaMailSender.send(message);
    }

    @Override
    public void sendUpdatePasswordToken(ResetPassword resetPassword) throws MessagingException {
        log.info("Отправляем токен для сброса пароля: {}", resetPassword.getEmail());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailAddress);

        helper.setTo(resetPassword.getEmail());
        helper.setSubject("Привет!");
        helper.setText(String.format("""
                Мы получили запрос на обновление пароля для твоего аккаунта.
                
                Новый пароль: %s
                                                            
                С уважением,
                Команда поддержки сайта
                """, resetPassword.getPassword()));
        javaMailSender.send(message);
    }
}
