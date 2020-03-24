package com.github.gr3gdev.jserver.http

import java.io.OutputStream

/**
 * Interface for responses.
 *
 * @author Gregory Tardivel
 */
interface Response {

    /**
     * HTTP Response Headers.
     *
     * @return headers
     */
    fun headers(): Map<String, String>

    /**
     * Response output stream.
     *
     * @return output
     */
    fun output(): OutputStream
}