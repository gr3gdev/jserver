package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.plugin.ServerPlugin
import java.io.OutputStream
import java.nio.charset.StandardCharsets

/**
 * RouteListener.
 *
 * @author Gregory Tardivel
 */
class RouteListener constructor() {

    private var responseData = Response()
    private var run: ((Route) -> Response)? = null
    private var plugins: Array<out ServerPlugin>? = null

    constructor(status: HttpStatus, contentType: String, content: ByteArray) : this() {
        responseData = Response(status, contentType, content)
    }

    constructor(status: HttpStatus, file: Response.File) : this() {
        responseData = Response(status, file)
    }

    /**
     * Process before rendered.
     */
    fun process(run: (Route) -> Response): RouteListener {
        this.run = run
        return this
    }

    private fun constructResponseHeader(response: Response): ByteArray {
        val headers = ArrayList<String>()
        if (response.redirect != null) {
            response.status = HttpStatus.FOUND
            headers.add("Location: ${response.redirect}")
        } else {
            headers.add("Content-Type: ${response.contentType}")
            headers.add("Content-Length: ${response.content.size}")
        }
        response.cookies.forEach { (key, value) -> headers.add("Set-Cookie: $key=$value") }
        return "HTTP/1.1 ${response.status.code}\r\n${headers.joinToString("\r\n")}\r\n\r\n"
                .toByteArray(StandardCharsets.UTF_8)
    }

    /**
     * Execute RouteListener.
     *
     * @param request HTTP Request
     * @param output Output stream for HTTP response
     */
    fun handleEvent(request: Request, output: OutputStream) {
        var response = this.responseData
        if (this.run != null) {
            response = this.run!!(Route(request, plugins))
        }
        Logger.debug("$request -- $response")
        output.write(constructResponseHeader(response).plus(response.content))
    }

    internal fun registerPlugins(serverPlugins: Array<out ServerPlugin>?) {
        plugins = serverPlugins
    }

}