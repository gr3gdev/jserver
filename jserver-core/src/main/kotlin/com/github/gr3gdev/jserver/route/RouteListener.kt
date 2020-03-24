package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.Response

/**
 * RouteListener.
 *
 * @author Gregory Tardivel
 */
interface RouteListener {

    /**
     * Execute RouteListener.
     *
     * @param request HTTP Request
     * @param response HTTP Response
     */
    fun handleEvent(request: Request, response: Response)
}