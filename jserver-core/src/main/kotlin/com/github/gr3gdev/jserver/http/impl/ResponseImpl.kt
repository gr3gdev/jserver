package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.Response
import java.io.OutputStream

/**
 * @author Gregory Tardivel
 */
class ResponseImpl(private val output: OutputStream) : Response {

    override fun write(data: ByteArray) {
        output.write(data)
    }

}