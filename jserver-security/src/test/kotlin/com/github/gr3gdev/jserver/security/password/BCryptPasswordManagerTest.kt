package com.github.gr3gdev.jserver.security.password

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BCryptPasswordManagerTest {

    @Test
    fun `Test validate password`() {
        val bCryptPasswordManager = BCryptPasswordManager(10)
        val encodedPassword = bCryptPasswordManager.encode("ARandomPassword10")
        assertTrue(bCryptPasswordManager.matches("ARandomPassword10", encodedPassword))
        assertFalse(bCryptPasswordManager.matches("ARandomPassword11", encodedPassword))
        assertFalse(bCryptPasswordManager.matches("I don't know", encodedPassword))
        assertFalse(bCryptPasswordManager.matches("", encodedPassword))
    }

}