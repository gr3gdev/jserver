package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.http.RequestMethod
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.socket.SocketEvent
import com.github.gr3gdev.jserver.socket.SocketReader
import java.io.IOException
import java.net.ServerSocket
import java.util.*

/**
 * jOwl Server.
 *
 * @author Gregory Tardivel
 */
class Server {

    private var active = false
    private val runnable: Runnable
    private var serverSocket: ServerSocket? = null
    private var port = 9000
    private val socketEvents: MutableList<SocketEvent> = ArrayList()

    init {
        socketEvents.add(SocketEvent("/favicon.ico", RequestMethod.GET,
                RouteListener(HttpStatus.OK, "/jserver.ico")))
    }

    fun port(pPort: Int): Server {
        port = pPort
        return this
    }

    @Synchronized
    fun start() {
        active = true
        Thread(runnable, "jServer").start()
    }

    @Synchronized
    fun stop() {
        active = false
    }

    /**
     * Process a GET Request.
     *
     * @param pPath          Path URL
     * @param pRouteListener Route listener
     * @return Server
     */
    fun get(pPath: String, pRouteListener: RouteListener): Server {
        process(pPath, RequestMethod.GET, pRouteListener)
        return this
    }

    /**
     * Process a POST Request.
     *
     * @param pPath          Path URL
     * @param pRouteListener Route listener
     * @return Server
     */
    fun post(pPath: String, pRouteListener: RouteListener): Server {
        process(pPath, RequestMethod.POST, pRouteListener)
        return this
    }

    /**
     * Process a Request.
     *
     * @param pPath          Path URL
     * @param pRequestMethod Method HTTP
     * @param pRouteListener Route listener
     * @return Server
     */
    private fun process(pPath: String, pRequestMethod: RequestMethod, pRouteListener: RouteListener) {
        socketEvents.add(SocketEvent(pPath, pRequestMethod, pRouteListener))
    }

    init {
        runnable = Runnable {
            if (serverSocket == null) {
                try {
                    serverSocket = ServerSocket(port)
                } catch (exc: IOException) {
                    exc.printStackTrace()
                }
            }
            Logger.info("Server started on port: $port")
            while (active) {
                try {
                    val socket = serverSocket!!.accept()
                    val threadSocket = Thread(SocketReader(socket, socketEvents), "jServer SocketReader")
                    threadSocket.start()
                } catch (exc: IOException) {
                    exc.printStackTrace()
                }
            }
            Logger.info("Server stopped")
        }
    }
}