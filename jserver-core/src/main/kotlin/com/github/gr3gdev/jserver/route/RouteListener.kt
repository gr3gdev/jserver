package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.Response
import java.nio.charset.StandardCharsets

/**
 * RouteListener.
 *
 * @author Gregory Tardivel
 */
class RouteListener constructor() {

    var responseData = ResponseData()
    private var run: ((Request) -> ResponseData)? = null

    constructor(status: HttpStatus, contentType: String, content: String) : this() {
        responseData.status = status
        responseData.contentType = contentType
        responseData.content = content.toByteArray()
    }

    constructor(status: HttpStatus, file: ResponseData.File) : this() {
        responseData.status = status
        responseData.file(file)
    }

    /**
     * Process before rendered.
     */
    fun process(run: (Request) -> ResponseData): RouteListener {
        this.run = run
        return this
    }

    private fun constructResponseHeader(): ByteArray {
        val headers = ArrayList<String>()
        if (responseData.redirect != null) {
            responseData.status = HttpStatus.FOUND
            headers.add("Location: ${responseData.redirect}")
        } else {
            headers.add("Content-Type: ${responseData.contentType}")
            headers.add("Content-Length: ${responseData.content.size}")
        }
        responseData.cookies.forEach { (key, value) -> headers.add("Set-Cookie: $key=$value") }
        return "HTTP/1.1 ${responseData.status.code}\r\n${headers.joinToString("\r\n")}\r\n\r\n"
                .toByteArray(StandardCharsets.UTF_8)
    }

    /**
     * Execute RouteListener.
     *
     * @param request HTTP Request
     * @param response HTTP Response
     */
    fun handleEvent(request: Request, response: Response) {
        if (this.run != null) {
            this.responseData = this.run!!(request)
        }
        response.write(constructResponseHeader().plus(this.responseData.content))
    }

}