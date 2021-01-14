package com.github.gr3gdev.jserver.samples.route

import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.RouteListener

object UserRoute {
    fun save(): RouteListener {
        return RouteListener().process { req, res ->
            val json = req.params("body")
            if (json != null) {
                // Execute something
                // ...
                res.status = HttpStatus.OK
                res.contentType = "application/json"
                res.content = json.toByteArray()
            } else {
                res.status = HttpStatus.NOT_FOUND
                res.contentType = "application/json"
                res.content = "{}".toByteArray()
            }
        }
    }
}