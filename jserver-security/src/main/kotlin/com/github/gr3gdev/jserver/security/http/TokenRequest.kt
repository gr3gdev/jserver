package com.github.gr3gdev.jserver.security.http

import com.github.gr3gdev.jserver.http.Request


object TokenRequest {

    private const val AUTH = "Authorization"
    private const val COOKIES = "Cookie"

    /**
     * Get JWT token from Authorization header.
     */
    fun <T> getTokenFromHeader(req: Request, ifPresent: (token: String) -> T, orElse: () -> T): T {
        // Token in Authorization
        return req.headers(AUTH, {
            if (it.startsWith("Bearer", true)) {
                ifPresent(it.substring("Bearer ".length))
            } else {
                orElse()
            }
        }, {
            orElse()
        })
    }

    /**
     * Get JWT token from Cookie.
     */
    fun <T> getTokenFromCookie(req: Request, cookieName: String, ifPresent: (token: String) -> T, orElse: () -> T): T {
        // Token in cookie
        return req.headers(COOKIES, { ch ->
            val cookies = ch.split(" ")
            val tokenCookie = cookies.find { c -> c.startsWith("$cookieName=") }.orEmpty()
            if (tokenCookie.isNotEmpty() && tokenCookie.contains("=")) {
                val tokenValue = tokenCookie.split("=")[1]
                val token = if (tokenValue.endsWith(";")) {
                    tokenValue.substring(0, tokenValue.length - 1)
                } else {
                    tokenValue
                }
                ifPresent(token)
            } else {
                orElse()
            }
        }, {
            orElse()
        })
    }

}
