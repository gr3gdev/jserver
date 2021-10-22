package com.github.gr3gdev.jserver.security.password;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BCryptPasswordManagerTest {

    @Test
    public void testValidatePassword() {
        final BCryptPasswordManager bCryptPasswordManager = new BCryptPasswordManager(10);
        final String encodedPassword = bCryptPasswordManager.encode("ARandomPassword10");
        assertTrue(bCryptPasswordManager.matches("ARandomPassword10", encodedPassword));
        assertFalse(bCryptPasswordManager.matches("ARandomPassword11", encodedPassword));
        assertFalse(bCryptPasswordManager.matches("I don't know", encodedPassword));
        assertFalse(bCryptPasswordManager.matches("", encodedPassword));
    }

}
