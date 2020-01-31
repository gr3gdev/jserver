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
        socket.use {
            // Response
            val response = ResponseImpl(it.getOutputStream())
            // Request
            val reader = BufferedReader(InputStreamReader(it.getInputStream()))
            val request = RequestImpl(it.remoteSocketAddress.toString(), reader)
            Logger.debug(request)
            // Search event
            socketEvents.filter { e -> e.match(request) }
                    .forEach { e -> e.routeListener.handleEvent(request, response) }
        }
    }

}
