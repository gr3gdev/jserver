package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.http.RequestMethod
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.ResponseData
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.socket.SocketEvent
import com.github.gr3gdev.jserver.socket.SocketReader
import java.io.IOException
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
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
                RouteListener(HttpStatus.OK, ResponseData.File("/favicon.ico", "image/vnd.microsoft.icon"))))
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
        return process(pPath, RequestMethod.GET, pRouteListener)
    }

    /**
     * Process a POST Request.
     *
     * @param pPath          Path URL
     * @param pRouteListener Route listener
     * @return Server
     */
    fun post(pPath: String, pRouteListener: RouteListener): Server {
        return process(pPath, RequestMethod.POST, pRouteListener)
    }

    /**
     * Process a PUT Request.
     *
     * @param pPath          Path URL
     * @param pRouteListener Route listener
     * @return Server
     */
    fun put(pPath: String, pRouteListener: RouteListener): Server {
        return process(pPath, RequestMethod.PUT, pRouteListener)
    }

    /**
     * Process a DELETE Request.
     *
     * @param pPath          Path URL
     * @param pRouteListener Route listener
     * @return Server
     */
    fun delete(pPath: String, pRouteListener: RouteListener): Server {
        return process(pPath, RequestMethod.DELETE, pRouteListener)
    }

    /**
     * Process a PATCH Request.
     *
     * @param pPath          Path URL
     * @param pRouteListener Route listener
     * @return Server
     */
    fun patch(pPath: String, pRouteListener: RouteListener): Server {
        return process(pPath, RequestMethod.PATCH, pRouteListener)
    }

    /**
     * Process a Request.
     *
     * @param pPath          Path URL
     * @param pRequestMethod Method HTTP
     * @param pRouteListener Route listener
     * @return Server
     */
    fun process(pPath: String, pRequestMethod: RequestMethod, pRouteListener: RouteListener): Server {
        socketEvents.add(SocketEvent(pPath, pRequestMethod, pRouteListener))
        return this
    }

    init {
        val bannerTxt = javaClass.getResourceAsStream("/banner.txt").readAllBytes()
        val properties = Properties()
        properties.load(javaClass.getResourceAsStream("/version.properties"))
        runnable = Runnable {
            if (serverSocket == null) {
                try {
                    serverSocket = ServerSocket(port)
                } catch (exc: IOException) {
                    Logger.error("Server error", exc)
                }
            }
            println(String(bannerTxt, StandardCharsets.UTF_8))
            Logger.info("jServer (${properties["version"]}) started on port $port")
            while (active) {
                try {
                    Thread(SocketReader(serverSocket!!.accept(), socketEvents), "jServer SocketReader").start()
                } catch (exc: IOException) {
                    Logger.error("Server socket error", exc)
                }
            }
            Logger.warn("Server stopped")
        }
    }
}