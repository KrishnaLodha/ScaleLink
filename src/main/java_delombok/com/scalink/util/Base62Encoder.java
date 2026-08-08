package com.scalink.util;

import java.security.SecureRandom;

public final class Base62Encoder {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();
    private static final SecureRandom RANDOM = new SecureRandom();

    private Base62Encoder() {
    }

    public static String encode(long value) {
        if (value == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }
        StringBuilder encoded = new StringBuilder();
        long current = value;
        while (current > 0) {
            encoded.append(ALPHABET.charAt((int) (current % BASE)));
            current /= BASE;
        }
        return encoded.reverse().toString();
    }

    public static long decode(String value) {
        long result = 0;
        for (int i = 0; i < value.length(); i++) {
            result = result * BASE + ALPHABET.indexOf(value.charAt(i));
        }
        return result;
    }

    public static String randomCode(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(ALPHABET.charAt(RANDOM.nextInt(BASE)));
        }
        return code.toString();
    }
}
