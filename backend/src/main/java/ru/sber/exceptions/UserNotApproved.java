package ru.sber.exceptions;

/**
 * Стоит выбрасывать если пользователь не одобрен
 */
public class UserNotApproved extends RuntimeException {
      public UserNotApproved(String message) {
        super(message);
    }
}
