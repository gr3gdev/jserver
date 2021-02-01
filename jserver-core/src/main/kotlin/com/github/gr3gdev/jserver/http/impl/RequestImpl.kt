package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.RemoteAddress
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.impl.ReaderUtil.loadHeaders
import com.github.gr3gdev.jserver.http.impl.ReaderUtil.loadParameters
import com.github.gr3gdev.jserver.logger.Logger
import java.io.BufferedReader
import java.util.*
import kotlin.collections.HashMap

/**
 * @author Gregory Tardivel
 */
class RequestImpl(private val remoteAddress: String, reader: BufferedReader) : Request {

    private val headers = HashMap<String, String>()
    private val parameters = HashMap<String, String>()
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

    private fun <T> conditional(optional: Optional<String>, ifPresent: (value: String) -> T, orElse: () -> T): T {
        var res: T? = null
        optional.ifPresentOrElse({
            res = ifPresent(it)
        }, {
            res = orElse()
        })
        return res!!
    }

    override fun headers(key: String): Optional<String> {
        return Optional.ofNullable(headers[key.toLowerCase()])
    }

    override fun <T> headers(key: String, ifPresent: (header: String) -> T, orElse: () -> T): T {
        return conditional(headers(key), ifPresent, orElse)
    }

    override fun headers(key: String, value: String) {
        headers[key.toLowerCase()] = value
    }

    override fun headersNames(): MutableSet<String> {
        return headers.keys
    }

    override fun params(key: String): Optional<String> {
        return Optional.ofNullable(parameters[key.toLowerCase()])
    }

    override fun <T> params(key: String, ifPresent: (param: String) -> T, orElse: () -> T): T {
        return conditional(params(key), ifPresent, orElse)
    }

    override fun params(key: String, value: String) {
        parameters[key.toLowerCase()] = value
    }

    override fun paramsNames(): MutableSet<String> {
        return parameters.keys
    }

    override fun remoteAddress(): RemoteAddress {
        return RemoteAddressImpl(remoteAddress)
    }

    override fun toString(): String {
        return "RequestImpl{httpMethod=$httpMethod, path=$path, protocol=$protocol, parameters=$parameters, headers=$headers}"
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
        loadHeaders(this, reader)
        // HTTP PARAMETERS
        var pathParameters: String? = null
        if (path.contains("?")) {
            pathParameters = path.substring(path.indexOf("?") + 1)
            path = path.substring(0, path.indexOf("?"))
        }
        loadParameters(this, pathParameters, reader)
    }
}