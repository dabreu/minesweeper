package com.minesweeper.security;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Class with utility methods to generate token, hash password and verify
 * authentication
 */
public class PasswordAuthenticator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt);
        byte[] hash = new byte[salt.length + dk.length];
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        return enc.encodeToString(hash);
    }

    private static byte[] pbkdf2(String password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }

    public static void checkPassword(String hashedPassword, String password) throws Exception {
        byte[] hash = Base64.getUrlDecoder().decode(hashedPassword);
        byte[] salt = Arrays.copyOfRange(hash, 0, 16);
        byte[] check = pbkdf2(password, salt);
        int zero = 0;
        for (int idx = 0; idx < check.length; ++idx)
            zero |= hash[salt.length + idx] ^ check[idx];
        if (zero != 0) {
            throw new InvalidLoginException();
        }
    }

}
