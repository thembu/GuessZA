package com.thembu.guessza.challenge;

import java.security.SecureRandom;

public final class CodeGenerator {
    // No 0/O, 1/I/L — avoids confusion when people type codes
    private static final char[] ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(int length) {
        char[] code = new char[length];
        for (int i = 0; i < length; i++) {
            code[i] = ALPHABET[RANDOM.nextInt(ALPHABET.length)];
        }
        return new String(code);
    }
}
