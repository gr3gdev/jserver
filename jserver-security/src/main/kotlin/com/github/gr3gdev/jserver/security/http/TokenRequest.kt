package com.github.gr3gdev.jserver.security.http

import com.github.gr3gdev.jserver.http.Request
import java.util.*


object TokenRequest {

    private const val AUTH = "Authorization"
    private const val COOKIES = "Cookie"

    /**
     * Get JWT token from Authorization header.
     */
    fun getTokenFromHeader(req: Request): Optional<String> {
        // Token in Authorization
        var token = Optional.empty<String>()
        req.headers(AUTH).ifPresent {
            if (it.startsWith("Bearer", true)) {
                token = Optional.of(it.substring("Bearer ".length))
            }
        }
        return token
    }

    /**
     * Get JWT token from Cookie.
     */
    fun getTokenFromCookie(req: Request, cookieName: String): Optional<String> {
        // Token in cookie
        var token = Optional.empty<String>()
        req.headers(COOKIES).ifPresent { ch ->
            val cookies = ch.split(" ")
            val tokenCookie = cookies.find { c -> c.startsWith("$cookieName=") }.orEmpty()
            if (tokenCookie.isNotEmpty() && tokenCookie.contains("=")) {
                val tokenValue = tokenCookie.split("=")[1]
                token = if (tokenValue.endsWith(";")) {
                    Optional.of(tokenValue.substring(0, tokenValue.length - 1))
                } else {
                    Optional.of(tokenValue)
                }

            }
        }
        return token
    }

}
