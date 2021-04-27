package com.github.gr3gdev.jserver.security

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.plugin.ServerPlugin
import com.github.gr3gdev.jserver.security.user.JwtData
import com.github.gr3gdev.jserver.security.user.UserData
import io.jsonwebtoken.Jwts
import java.security.PublicKey
import java.util.*

class TokenClientPlugin(private val key: PublicKey) : ServerPlugin {

    /**
     * Get user data from JWT token.
     */
    fun <T : UserData> getUserData(token: String?, clazz: Class<T>): Optional<JwtData<T>> {
        if (token != null) {
            try {
                val claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token)
                return Optional.of(JwtData(claims.body, clazz))
            } catch (exc: Exception) {
                Logger.error("JWT UserData error", exc)
            }
        }
        return Optional.empty()
    }

}