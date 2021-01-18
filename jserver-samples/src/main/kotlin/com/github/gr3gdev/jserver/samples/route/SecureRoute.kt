package com.github.gr3gdev.jserver.samples.route

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.ResponseData
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.samples.bean.User
import com.github.gr3gdev.jserver.security.TokenManager

object SecureRoute {

    fun get(tokenExtractor: TokenManager) = RouteListener().process { request, responseData ->
        tokenExtractor.getTokenFromCookie(request, "MY_AUTH_COOKIE").ifPresentOrElse({ token ->
            tokenExtractor.getUserData(token, User::class.java).ifPresentOrElse({ userToken ->
                Logger.debug("User: $userToken")
                responseData.status = HttpStatus.OK
                responseData.file(ResponseData.File("/pages/secure.html", "text/html"))
            }, {
                Logger.error("Authentication invalid")
                responseData.redirect = "/login"
            })
        }, {
            Logger.error("Authentication not found")
            responseData.redirect = "/login"
        })
    }

}