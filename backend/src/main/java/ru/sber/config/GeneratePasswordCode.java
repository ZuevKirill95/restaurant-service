package ru.sber.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class GeneratePasswordCode {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    private GeneratePasswordCode() {
    }

    public static String generateOneTimeToken() {
        try {
            // Создание случайной последовательности байтов
            byte[] randomBytes = new byte[32];

            secureRandom.nextBytes(randomBytes);

            // Хеширование случайной последовательности с использованием SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(randomBytes);

            // Кодирование хеша в Base64
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        // Создание строки символов, включающей нужные символы
        String characters = LOWER + UPPER + DIGITS;

        // Генерация пароля
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    public static String hashPassword(String password) {
        try {
            // Создание экземпляра MessageDigest с алгоритмом SHA-256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // Вычисление хеша пароля
            byte[] hashedBytes = messageDigest.digest(password.getBytes());

            // Преобразование хеша в строку
            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
