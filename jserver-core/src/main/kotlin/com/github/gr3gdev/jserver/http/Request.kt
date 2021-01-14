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
     * Get HTTP Request Headers.
     *
     * @return headers
     */
    fun headers(key: String): String?

    /**
     * Put HTTP Request Headers.
     *
     * @return headers
     */
    fun headers(key: String, value: String?)

    /**
     * Get HTTP Request Headers names.
     */
    fun headersNames(): MutableSet<String>

    /**
     * Get HTTP Request Parameters.
     *
     * @return params
     */
    fun params(key: String): String?

    /**
     * Put HTTP Request Parameters.
     *
     * @return params
     */
    fun params(key: String, value: String?)

    /**
     * Get HTTP Request Parameters names.
     */
    fun paramsNames(): MutableSet<String>

    /**
     * Client remote address.
     *
     * @return remoteAddress
     */
    fun remoteAddress(): RemoteAddress
}