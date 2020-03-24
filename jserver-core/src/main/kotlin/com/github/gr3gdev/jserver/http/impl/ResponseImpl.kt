package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.Response
import java.io.OutputStream
import java.util.*

/**
 * @author Gregory Tardivel
 */
class ResponseImpl(pOutputStream: OutputStream) : Response {

    private val headers: Map<String, String>
    private val output: OutputStream

    override fun headers(): Map<String, String> {
        return headers
    }

    override fun output(): OutputStream {
        return output
    }

    init {
        headers = HashMap()
        output = pOutputStream
    }
}