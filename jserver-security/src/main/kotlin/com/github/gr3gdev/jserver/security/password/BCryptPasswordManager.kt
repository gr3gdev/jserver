package com.github.gr3gdev.jserver.security.password

import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom

class BCryptPasswordManager(private val strength: Int) {

    private val random = SecureRandom()

    /**
     * Encode password with BCrypt.
     * @param password Password
     */
    fun encode(password: String): String {
        val salt = generateSalt()
        return BCrypt.hashpw(password, salt)
    }

    /**
     * Check password.
     * @param password plain text
     * @param encoded password hashed
     */
    fun matches(password: String, encoded: String): Boolean {
        return BCrypt.checkpw(password, encoded)
    }

    private fun generateSalt(): String {
        return BCrypt.gensalt(strength, random)
    }

}