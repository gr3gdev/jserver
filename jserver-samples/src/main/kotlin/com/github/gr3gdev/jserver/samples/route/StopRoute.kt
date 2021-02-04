package com.github.gr3gdev.jserver.samples.route

import com.github.gr3gdev.jserver.Server
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener

object StopRoute {

    fun exec(server: Server) = RouteListener().process {
        server.stop()
        Response(HttpStatus.OK)
    }

}