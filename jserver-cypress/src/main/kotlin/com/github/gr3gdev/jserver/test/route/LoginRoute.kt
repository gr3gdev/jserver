package com.github.gr3gdev.jserver.test.route

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.security.TokenServerPlugin
import com.github.gr3gdev.jserver.security.password.BCryptPasswordManager
import com.github.gr3gdev.jserver.test.bean.User

object LoginRoute {

    private val bCryptPasswordManager = BCryptPasswordManager(10)
    private val user = User("user", bCryptPasswordManager.encode("password"))

    fun get() = RouteListener(HttpStatus.OK, Response.File("/pages/login.html", "text/html"))

    fun post() = RouteListener().process { route ->
        var res = Response(HttpStatus.OK)
                .redirect("/login")
        val request = route.request
        request.params("username").ifPresent { username ->
            request.params("password").ifPresent { password ->
                if (username == user.username && bCryptPasswordManager.matches(password, user.password)) {
                    Logger.debug("User authenticated")
                    val token = route.plugin(TokenServerPlugin::class.java)
                            .createToken(user, 60 * 60 * 1000)
                    res = Response(HttpStatus.OK)
                            .cookie(Response.Cookie("MY_AUTH_COOKIE", token))
                            .cookie(Response.Cookie("TEST", "OK", 600,
                                    path = "/test", secure = true))
                            .redirect("/secure")
                } else {
                    Logger.error("User or password incorrect")
                }
            }
        }
        res
    }

}