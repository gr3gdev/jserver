package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.Response

/**
 * RouteListener.
 *
 * @author Gregory Tardivel
 */
class RouteListener(private val status: HttpStatus, private val contentType: String, private val content: String) {

    /**
     * Execute RouteListener.
     *
     * @param request HTTP Request
     * @param response HTTP Response
     */
    fun handleEvent(request: Request, response: Response) {
        val headers = "HTTP/1.1 $status\r\nContent-Type: $contentType\r\nContent-Length: ${content.length}\r\n\r\n"
        response.output().use {
            it.write((headers + content).toByteArray())
        }
    }

}