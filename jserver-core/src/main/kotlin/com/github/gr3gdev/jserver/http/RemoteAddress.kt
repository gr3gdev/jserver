package com.github.gr3gdev.jserver.http

/**
 * Interface for remote address.
 *
 * @author Gregory Tardivel
 */
interface RemoteAddress {

    /**
     * Remote socket address IP.
     *
     * @return ip
     */
    fun ip(): String

    /**
     * Remote socket address port.
     *
     * @return port
     */
    fun port(): String

}