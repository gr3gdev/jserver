package com.github.gr3gdev.jserver.socket

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.impl.RequestImpl
import com.github.gr3gdev.jserver.http.Response
import com.github.gr3gdev.jserver.http.impl.ResponseImpl
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

/**
 * SocketReader.
 *
 * @author Gregory Tardivel
 */
internal class SocketReader(private val socket: Socket, private val socketEvents: List<SocketEvent>) : Runnable {

    override fun run() {
        try {
            val input = socket.getInputStream()
            // Response
            val response: Response = ResponseImpl(socket.getOutputStream())
            val reader = BufferedReader(InputStreamReader(input))
            // Request
            val request: Request = RequestImpl(reader)
            println(request)
            // Search event
            for (event in socketEvents) { // Event by HTTP method & by Path
                if (event.method.name.equals(request.method(), ignoreCase = true)
                        && event.path == request.path()) {
                    // Execute event
                    event.routeListener.handleEvent(request, response)
                }
            }
        } catch (exc: IOException) {
            exc.printStackTrace()
        }
    }

}