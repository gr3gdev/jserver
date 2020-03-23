package com.github.gr3gdev.jserver.socket

import com.github.gr3gdev.jserver.http.RequestMethod
import com.github.gr3gdev.jserver.route.RouteListener

/**
 * SocketEvent.
 *
 * @author Gregory Tardivel
 */
internal class SocketEvent(val path: String, val method: RequestMethod, val routeListener: RouteListener)