package com.github.gr3gdev.jserver.samples

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.samples.route.LoginRoute
import com.github.gr3gdev.jserver.samples.route.SecureRoute
import com.github.gr3gdev.jserver.security.TokenExtractor

fun main() {
    val tokenExtractor = TokenExtractor.id("jServerDemo").issuer("GR3Gdev")
            .generateSecretKey(256)
    Logger.changeLevel(Logger.Level.DEBUG)
    JServer.server()
            .port(9090)
            .get("/secure", SecureRoute.get(tokenExtractor))
            .get("/login", LoginRoute.get())
            .post("/login", LoginRoute.post(tokenExtractor))
            .start()
}
