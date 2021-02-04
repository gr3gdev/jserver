package com.github.gr3gdev.jserver.socket

import com.github.gr3gdev.jserver.http.impl.RequestImpl
import java.net.Socket

/**
 * SocketReader.
 *
 * @author Gregory Tardivel
 */
internal class SocketReader(private val socket: Socket, private val socketEvents: List<SocketEvent>) : Runnable {

    override fun run() {
        socket.use {
            // Request
            val request = RequestImpl(it.remoteSocketAddress.toString(), it.getInputStream())
            // Search event
            socketEvents.filter { e -> e.match(request) }
                    .forEach { e -> e.routeListener.handleEvent(request, it.getOutputStream()) }
        }
    }

}
