package com.app.pharmacy.util;

import java.util.Random;

public class StringUtils {
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A');
            builder.append(randomChar);
        }
        return builder.toString();
    }

    public static String generateRandomNumberString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomChar = random.nextInt(9);
            builder.append(randomChar);
        }
        return builder.toString();
    }
}
