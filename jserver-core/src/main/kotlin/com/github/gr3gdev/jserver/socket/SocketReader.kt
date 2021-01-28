package com.github.gr3gdev.jserver.socket

import com.github.gr3gdev.jserver.http.impl.RequestImpl
import com.github.gr3gdev.jserver.http.impl.ResponseImpl
import com.github.gr3gdev.jserver.logger.Logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

/**
 * SocketReader.
 *
 * @author Gregory Tardivel
 */
internal class SocketReader(private val socket: Socket, private val socketEvents: List<SocketEvent>) : Runnable {

    override fun run() {
        socket.getInputStream().use { input ->
            socket.getOutputStream().use { output ->
                // Response
                val response = ResponseImpl(output)
                BufferedReader(InputStreamReader(input)).use { reader ->
                    // Request
                    val request = RequestImpl(socket.remoteSocketAddress.toString(), reader)
                    Logger.debug(request)
                    // Search event
                    socketEvents.filter { it.match(request) }
                            .forEach { it.routeListener.handleEvent(request, response) }
                }
            }
        }
    }

}