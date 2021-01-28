package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.Response
import java.io.OutputStream

/**
 * @author Gregory Tardivel
 */
class ResponseImpl(private val pOutputStream: OutputStream) : Response {

    override fun output(): OutputStream {
        return pOutputStream
    }

}