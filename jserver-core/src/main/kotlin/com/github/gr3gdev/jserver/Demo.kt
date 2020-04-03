package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.RouteListener

fun main() {
    JServer.server()
            .get("/", RouteListener(HttpStatus.OK, "text/html",
                    "<!DOCTYPE HTML>" +
                            "<html>" +
                            "<head><title>DEMO</title></head>" +
                            "<body>It works !</body>" +
                            "</html>"))
            .start()
}
