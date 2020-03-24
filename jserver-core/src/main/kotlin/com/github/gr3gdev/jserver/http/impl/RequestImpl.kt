package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.impl.ReaderUtil.loadHeaders
import com.github.gr3gdev.jserver.http.impl.ReaderUtil.loadParameters
import com.github.gr3gdev.jserver.http.Request
import java.io.BufferedReader
import java.util.*

/**
 * @author Gregory Tardivel
 */
class RequestImpl(reader: BufferedReader) : Request {

    private val headers: Map<String, String>
    private val parameters: Map<String, String>
    private var httpMethod = ""
    private var path = ""
    private var protocol = ""

    override fun path(): String {
        return path
    }

    override fun method(): String {
        return httpMethod
    }

    override fun protocol(): String {
        return protocol
    }

    override fun headers(): Map<String, String> {
        return headers
    }

    override fun params(): Map<String, String> {
        return parameters
    }

    override fun toString(): String {
        return "RequestImpl{" +
                "httpMethod='" + httpMethod + '\'' +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                '}'
    }

    init {
        val requestLine = reader.readLine()
        if (requestLine != null) {
            val tokens = StringTokenizer(requestLine)
            // HTTP Method (GET, POST, ...)
            httpMethod = tokens.nextToken()
            if (tokens.hasMoreTokens()) { // Path
                path = tokens.nextToken()
            }
            if (tokens.hasMoreTokens()) { // Protocol HTTP
                protocol = tokens.nextToken()
            }
        }
        // HEADERS
        headers = loadHeaders(reader)
        // HTTP PARAMETERS
        parameters = loadParameters(reader)
    }
}