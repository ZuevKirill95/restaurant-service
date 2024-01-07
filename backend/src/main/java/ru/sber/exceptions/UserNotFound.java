package ru.sber.exceptions;

/**
 * Стоит выбрасывать если пользователь не найден
 */
public class UserNotFound extends RuntimeException {
      public UserNotFound(String message) {
        super(message);
    }
}
