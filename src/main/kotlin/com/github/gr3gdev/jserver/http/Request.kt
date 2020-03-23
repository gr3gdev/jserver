package com.github.gr3gdev.jserver.http

/**
 * Interface for requests.
 *
 * @author Gregory Tardivel
 */
interface Request {

    /**
     * Path URL.
     *
     * @return path
     */
    fun path(): String

    /**
     * HTTP Method.
     *
     * @return method
     */
    fun method(): String

    /**
     * HTTP Protocol.
     *
     * @return protocol
     */
    fun protocol(): String

    /**
     * HTTP Request Headers.
     *
     * @return headers
     */
    fun headers(): Map<String, String>

    /**
     * HTTP Request Parameters.
     *
     * @return params
     */
    fun params(): Map<String, String>
}