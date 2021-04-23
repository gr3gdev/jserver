package com.github.gr3gdev.jserver.test.route

import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.security.TokenClientPlugin
import com.github.gr3gdev.jserver.security.http.TokenRequest
import com.github.gr3gdev.jserver.test.bean.User

object SecureRoute {

    fun get() = RouteListener().process { route ->
        TokenRequest.getTokenFromCookie(route.request, "MY_AUTH_COOKIE", { token ->
            route.plugin(TokenClientPlugin::class.java)
                    .getUserData(token, User::class.java, { userToken ->
                        Logger.debug("User: $userToken")
                        Response(HttpStatus.OK, Response.File("/pages/secure.html", "text/html"))
                    }, {
                        Logger.error("Authentication invalid")
                        Response("/login")
                    })
        }, {
            Logger.error("Authentication not found")
            Response("/login")
        })
    }

}