package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.route.html.HtmlRouteListener
import com.github.gr3gdev.jserver.route.html.HttpStatus

fun main() {
    val home = HtmlRouteListener()
    home.response(HttpStatus.OK, "<!DOCTYPE HTML><html><head><title>Demo</title></head>" +
            "<body><h1>It works !</h1></body></html>")

    JServer.server()
            .get("/", home)
            .start()
}
