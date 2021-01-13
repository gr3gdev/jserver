package com.github.gr3gdev.jserver.samples.route

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.ResponseData
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.samples.bean.User
import com.github.gr3gdev.jserver.security.TokenExtractor

object SecureRoute {

    fun get(tokenExtractor: TokenExtractor) = RouteListener().process { request, responseData ->
        val userToken = tokenExtractor.getUserData(request, User::class.java)
        Logger.debug("User: ${userToken.toString()}")
        if (userToken == null) {
            responseData.redirect = "/login"
        } else {
            responseData.status = HttpStatus.OK
            responseData.file(ResponseData.File("/pages/secure.html", "text/html"))
        }
    }

}