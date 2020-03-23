package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.Response
import com.github.gr3gdev.jserver.route.RouteListener

fun main() {
    val home = object : RouteListener {
        override fun handleEvent(request: Request, response: Response) {
            response.output().use {
                it.write("It works !".toByteArray())
            }
        }
    }
    JServer.server()
            .get("/", home)
            .start()
}
