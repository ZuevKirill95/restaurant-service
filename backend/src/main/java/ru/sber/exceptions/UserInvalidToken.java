package ru.sber.exceptions;

/**
 * Стоит выбрасывать если пользователь не одобрен
 */
public class UserInvalidToken extends RuntimeException {
      public UserInvalidToken(String message) {
        super(message);
    }
}
