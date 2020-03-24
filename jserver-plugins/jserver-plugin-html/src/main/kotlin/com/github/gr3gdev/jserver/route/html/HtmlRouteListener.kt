package com.github.gr3gdev.jserver.route.html

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.Response
import com.github.gr3gdev.jserver.route.RouteListener

class HtmlRouteListener : RouteListener {

    private lateinit var status: String
    private lateinit var content: String

    override fun handleEvent(request: Request, response: Response) {
        val headers = "HTTP/1.1 $status\r\nContent-Type: text/html\r\nContent-Length: ${content.length}\r\n\r\n"
        response.output().use {
            it.write((headers + content).toByteArray())
        }
    }

    fun response(httpStatus: HttpStatus, html: String) {
        status = httpStatus.code
        content = html
    }

}