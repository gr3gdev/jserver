package com.github.gr3gdev.jserver.samples.route

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.ResponseData
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.samples.bean.User
import com.github.gr3gdev.jserver.security.TokenManager
import com.github.gr3gdev.jserver.security.password.BCryptPasswordManager

object LoginRoute {

    private val bCryptPasswordManager = BCryptPasswordManager(10)
    private val user = User("user", bCryptPasswordManager.encode("password"))

    fun get() = RouteListener(HttpStatus.OK, ResponseData.File("/pages/login.html", "text/html"))

    fun post(tokenManager: TokenManager) = RouteListener().process { request, responseData ->
        val username = request.params()["username"]
        val password = request.params()["password"]
        if (username == user.username && bCryptPasswordManager.matches(password!!, user.password)) {
            Logger.debug("User authenticated")
            val token = tokenManager.createToken(user, 60 * 60 * 1000)
            responseData.cookies["MY_AUTH_COOKIE"] = token
            responseData.cookies["TEST"] = "OK"
            responseData.redirect = "/secure"
        } else {
            Logger.error("User or password incorrect")
            responseData.redirect = "/login"
        }
    }

}