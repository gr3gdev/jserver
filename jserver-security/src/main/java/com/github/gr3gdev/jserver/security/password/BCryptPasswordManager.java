package com.github.gr3gdev.jserver.security.password;

import org.bouncycastle.crypto.generators.BCrypt;

import java.nio.charset.StandardCharsets;

/**
 * BCryptPasswordManager.
 *
 * @author Gregory Tardivel
 */
public class BCryptPasswordManager {

    private final int strength;

    public BCryptPasswordManager(final int strength) {
        this.strength = strength;
    }

    /**
     * Encode password with BCrypt.
     *
     * @param password Password
     * @return String
     */
    public String encode(final String password) {
        return new String(
                BCrypt.generate(
                        password.getBytes(StandardCharsets.UTF_8),
                        new byte[16],
                        strength)
        );
    }

    /**
     * Check password.
     *
     * @param password plain text
     * @param encoded  password hashed
     * @return boolean
     */
    public boolean matches(final String password, final String encoded) {
        return encoded.equals(encode(password));
    }

}
