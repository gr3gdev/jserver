package com.github.gr3gdev.jserver.test.route

import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener

object ReactRoute {

    private val mimeTypes = mapOf(
            ".html" to "text/html",
            ".js" to "application/javascript",
            ".css" to "text/css",
            ".png" to "image/png",
            ".json" to "application/json",
            ".map" to "application/json",
            ".svg" to "image/svg+xml",
            ".txt" to "text/plain",
            ".ico" to "image/x-icon"
    )

    fun get() = RouteListener().process {
        Response(HttpStatus.OK, Response.File("/react/index.html", "text/html"))
    }

    fun static(path: String) = RouteListener().process { route ->
        var res = Response(HttpStatus.NOT_FOUND)
        route.request.params("file").ifPresent { file ->
            val contentType = mimeTypes.entries.firstOrNull { file.endsWith(it.key) }?.value ?: "text/plain"
            res = Response(HttpStatus.OK, Response.File("$path/$file", contentType))
        }
        res
    }

}