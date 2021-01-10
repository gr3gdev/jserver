package com.github.gr3gdev.jserver.http

import java.io.OutputStream

/**
 * Interface for responses.
 *
 * @author Gregory Tardivel
 */
interface Response {

    /**
     * Response output stream.
     *
     * @return output
     */
    fun output(): OutputStream
}