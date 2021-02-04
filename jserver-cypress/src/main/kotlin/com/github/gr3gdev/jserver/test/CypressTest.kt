package com.github.gr3gdev.jserver.test

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.test.route.ApiRoute
import com.github.gr3gdev.jserver.test.route.ReactRoute
import com.github.gr3gdev.jserver.test.route.StopRoute

fun main() {
    Logger.changeLevel(Logger.Level.DEBUG)
    val server = JServer.server()
            // REACT front
            .get("/react/{file}", ReactRoute.static("/react"))
            .get("/react", ReactRoute.get())
            // API REST
            .get("/api/persons/{id}", ApiRoute.findById())
            .get("/api/persons", ApiRoute.findAll())
            .put("/api/persons", ApiRoute.save())
    // STOP
    server.get("/stop", StopRoute.exec(server))
            .start()
}
