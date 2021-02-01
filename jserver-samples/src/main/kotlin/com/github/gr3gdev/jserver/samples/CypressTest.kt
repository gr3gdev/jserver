package com.github.gr3gdev.jserver.samples

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.samples.route.LoginRoute
import com.github.gr3gdev.jserver.samples.route.SecureRoute
import com.github.gr3gdev.jserver.samples.route.StopRoute
import com.github.gr3gdev.jserver.samples.route.UserRoute
import com.github.gr3gdev.jserver.security.TokenManager

fun main() {
    val tokenExtractor = TokenManager.id("jServerCypressTests").issuer("GR3Gdev")
            .generateSecretKey(256)
    Logger.changeLevel(Logger.Level.DEBUG)
    val server = JServer.server()
            .get("/secure", SecureRoute.get(tokenExtractor))
            .get("/login", LoginRoute.get())
            .post("/login", LoginRoute.post(tokenExtractor))
            .put("/api/users", UserRoute.save())
            .get("/api/users", UserRoute.get())
    server.get("/stop", StopRoute.exec(server))
            .start()
}
