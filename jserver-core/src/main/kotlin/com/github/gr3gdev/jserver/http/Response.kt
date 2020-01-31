package com.github.gr3gdev.jserver.http

/**
 * Interface for responses.
 *
 * @author Gregory Tardivel
 */
interface Response {

    /**
     * Write a response.
     */
    fun write(data: ByteArray)
}