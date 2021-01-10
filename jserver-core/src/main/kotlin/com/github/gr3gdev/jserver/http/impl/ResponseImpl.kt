package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.Response
import java.io.OutputStream

/**
 * @author Gregory Tardivel
 */
class ResponseImpl(pOutputStream: OutputStream) : Response {

    private val output = pOutputStream

    override fun output(): OutputStream {
        return output
    }

}