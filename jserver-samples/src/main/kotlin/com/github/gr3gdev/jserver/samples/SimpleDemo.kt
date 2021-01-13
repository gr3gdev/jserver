package com.github.gr3gdev.jserver.samples

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.RouteListener

fun main() {
    JServer.server()
            .get("/", RouteListener(HttpStatus.OK, "text/html",
                    "<!DOCTYPE HTML>" +
                            "<html>" +
                            "<head><title>DEMO</title></head>" +
                            "<body>" +
                            "<h1>It works !</h1>" +
                            "<form method='POST' action='/test'>" +
                            "<input type='text' name='message' placeholder='message'/>" +
                            "<input type='submit' name='action' value='send'/>" +
                            "</form>" +
                            "</body>" +
                            "</html>"))
            .post("/test", RouteListener().process { req, route ->
                val msg = req.params()["message"]
                route.status = HttpStatus.OK
                route.contentType = "text/html"
                route.content = ("<!DOCTYPE HTML>" +
                        "<html>" +
                        "<head><title>DEMO</title></head>" +
                        "<body>" +
                        "<h1>$msg</h1>" +
                        "<form method='POST' action='/test'>" +
                        "<input type='text' name='message' placeholder='message'/>" +
                        "<input type='submit' name='action' value='send'/>" +
                        "</form>" +
                        "</body>" +
                        "</html>").toByteArray()
            })
            .get("/json", RouteListener().process { _, route ->
                route.status = HttpStatus.OK
                route.contentType = "application/json"
                route.content = "{\"name\":\"Test\", \"status\":\"SUCCESS\"}".toByteArray()
            })
            .get("/forbidden", RouteListener().process { _, route ->
                route.status = HttpStatus.FORBIDDEN
                route.contentType = "text/plain"
                route.content = "Access forbidden !".toByteArray()
            })
            .start()
}
