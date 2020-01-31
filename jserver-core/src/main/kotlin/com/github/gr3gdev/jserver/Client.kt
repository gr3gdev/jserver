package com.github.gr3gdev.jserver

import java.io.DataOutputStream
import java.net.Socket

/**
 * Client for server's connections.
 *
 * @author Gregory Tardivel
 */
class Client {

    private lateinit var socket: Socket

    /**
     * Connection to server.
     *
     * @param host Host address
     * @param port Port number
     */
    fun connect(host: String, port: Int): Client {
        socket = Socket(host, port)
        return this
    }

    /**
     * Send a message to server.
     *
     * @param message Text message
     */
    fun send(message: String) {
        val output = DataOutputStream(socket.getOutputStream())
        output.write(message.toByteArray())
    }

}