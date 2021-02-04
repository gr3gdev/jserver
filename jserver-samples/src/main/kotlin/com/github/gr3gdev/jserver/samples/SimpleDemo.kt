package com.github.gr3gdev.jserver.samples

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener

fun main() {
    JServer.server()
            .get("/", RouteListener(HttpStatus.OK, "text/html", """
                <!DOCTYPE HTML>
                <html>
                    <head><title>DEMO</title></head>
                    <body>
                        <h1>It works !</h1>
                        <form method='POST' action='/test'>
                            <input type='text' name='message' placeholder='message'/>
                            <input type='submit' name='action' value='send'/>
                        </form>
                    </body>
                </html>
            """.trimIndent().toByteArray()))
            .post("/test", RouteListener().process { req ->
                req.params("message", {
                    Response(HttpStatus.OK, "text/html", ("<!DOCTYPE HTML>" +
                            "<html>" +
                            "<head><title>DEMO</title></head>" +
                            "<body>" +
                            "<h1>$it</h1>" +
                            "<form method='POST' action='/test'>" +
                            "<input type='text' name='message' placeholder='message'/>" +
                            "<input type='submit' name='action' value='send'/>" +
                            "</form>" +
                            "</body>" +
                            "</html>").toByteArray())
                }, {
                    Response(HttpStatus.INTERNAL_SERVER_ERROR)
                })
            }).start()
}
