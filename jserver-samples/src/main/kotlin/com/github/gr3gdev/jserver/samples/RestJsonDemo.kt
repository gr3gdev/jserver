package com.github.gr3gdev.jserver.samples

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.samples.route.UserRoute

fun main() {
    Logger.changeLevel(Logger.Level.DEBUG)
    JServer.server().port(3000)
            .post("/api/user", UserRoute.save())
            .get("/api/user", UserRoute.get())
            .start()
}
