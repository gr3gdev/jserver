package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.http.RequestMethod
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.plugin.ServerPlugin
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
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
    private var serverPlugins: Array<out ServerPlugin>? = null

    private var startupEvent: (() -> Unit)? = null

    init {
        socketEvents.add(SocketEvent("/favicon.ico", RequestMethod.GET,
                RouteListener(HttpStatus.OK, Response.File("/favicon.ico", "image/vnd.microsoft.icon"))))
    }

    fun port(pPort: Int): Server {
        port = pPort
        return this
    }

    @Synchronized
    fun start(): Server {
        active = true
        Thread(runnable, "jServer").start()
        return this
    }

    @Synchronized
    fun stop() {
        active = false
        if (serverSocket != null) {
            serverSocket!!.close()
        }
    }

    @Synchronized
    fun isAlive(): Boolean {
        return active
    }

    @Synchronized
    fun onStartup(event: () -> Unit): Server {
        startupEvent = event
        return this
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
        pRouteListener.registerPlugins(serverPlugins)
        socketEvents.add(SocketEvent(pPath, pRequestMethod, pRouteListener))
        return this
    }

    /**
     * Add plugins.
     *
     * @param pPlugins List of plugins
     * @return Server
     */
    fun plugins(vararg pPlugins: ServerPlugin): Server {
        serverPlugins = pPlugins
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
                    throw RuntimeException(exc)
                }
            }
            println(String(bannerTxt, StandardCharsets.UTF_8))
            Logger.info("jServer (${properties["version"]}) started on port $port")
            if (startupEvent != null) {
                startupEvent!!()
            }
            while (active) {
                try {
                    Thread(SocketReader(serverSocket!!.accept(), socketEvents), "jServer SocketReader").start()
                } catch (exc: IOException) {
                    if (active) {
                        Logger.error("Server socket error", exc)
                    }
                }
            }
            Logger.warn("Server stopped")
        }
    }
}
