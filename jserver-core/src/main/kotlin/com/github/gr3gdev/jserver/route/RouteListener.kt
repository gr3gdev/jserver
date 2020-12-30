package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.Response

/**
 * RouteListener.
 *
 * @author Gregory Tardivel
 */
class RouteListener constructor() {

    var responseData = ResponseData()
    private var run: ((Request, ResponseData) -> Unit)? = null

    constructor(status: HttpStatus, contentType: String, content: String) : this() {
        responseData.status = status
        responseData.contentType = contentType
        responseData.content = content
    }

    /**
     * Process before rendered.
     */
    fun process(run: (Request, ResponseData) -> Unit): RouteListener {
        this.run = run
        return this
    }

    /**
     * Execute RouteListener.
     *
     * @param request HTTP Request
     * @param response HTTP Response
     */
    fun handleEvent(request: Request, response: Response) {
        if (this.run != null) {
            this.run!!(request, responseData)
        }
        val headers = "HTTP/1.1 ${responseData.status.code}\r\nContent-Type: ${responseData.contentType}\r\nContent-Length: ${responseData.content.length}\r\n\r\n"
        response.output().use {
            it.write((headers + responseData.content).toByteArray())
        }
    }

}