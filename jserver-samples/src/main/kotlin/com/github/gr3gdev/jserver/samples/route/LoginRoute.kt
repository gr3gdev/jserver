package com.github.gr3gdev.jserver.samples.route

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.samples.bean.User
import com.github.gr3gdev.jserver.security.TokenManager
import com.github.gr3gdev.jserver.security.password.BCryptPasswordManager

object LoginRoute {

    private val bCryptPasswordManager = BCryptPasswordManager(10)
    private val user = User("user", bCryptPasswordManager.encode("password"))

    fun get() = RouteListener(HttpStatus.OK, Response.File("/pages/login.html", "text/html"))

    fun post(tokenManager: TokenManager) = RouteListener().process { request ->
        request.params("username", { username ->
            request.params("password", { password ->
                if (username == user.username && bCryptPasswordManager.matches(password, user.password)) {
                    Logger.debug("User authenticated")
                    val token = tokenManager.createToken(user, 60 * 60 * 1000)
                    Response(HttpStatus.OK)
                            .cookie("MY_AUTH_COOKIE", token)
                            .cookie("TEST", "OK")
                            .redirect("/secure")
                } else {
                    Logger.error("User or password incorrect")
                    Response(HttpStatus.OK)
                            .redirect("/login")
                }
            }, {
                Logger.error("Password is empty")
                Response(HttpStatus.OK)
                        .redirect("/login")
            })
        }, {
            Logger.error("Username is empty")
            Response(HttpStatus.OK)
                    .redirect("/login")
        })
    }

}