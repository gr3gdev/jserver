package com.github.gr3gdev.jserver.samples

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.samples.route.UserRoute

fun main() {
    JServer.server().port(3000)
            .post("/api/user", UserRoute.save())
            .start()
}
