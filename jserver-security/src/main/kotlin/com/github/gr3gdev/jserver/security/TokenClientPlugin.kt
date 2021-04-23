package com.github.gr3gdev.jserver.security

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.plugin.ServerPlugin
import com.github.gr3gdev.jserver.security.user.JwtData
import com.github.gr3gdev.jserver.security.user.UserData
import io.jsonwebtoken.Jwts
import java.security.PublicKey

class TokenClientPlugin(private val key: PublicKey) : ServerPlugin {

    /**
     * Get user data from JWT token.
     */
    fun <T : UserData, R> getUserData(token: String?, clazz: Class<T>,
                                      ifPresent: (data: JwtData<T>) -> R, orElse: () -> R): R {
        return if (token != null) {
            try {
                val claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token)
                ifPresent(JwtData(claims.body, clazz))
            } catch (exc: Exception) {
                Logger.error("JWT UserData error", exc)
                orElse()
            }
        } else {
            orElse()
        }
    }

}