package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import java.io.BufferedReader
import java.io.IOException
import java.util.*

/**
 * @author Gregory Tardivel
 */
internal object ReaderUtil {

    @Throws(IOException::class)
    fun loadHeaders(request: Request, pReader: BufferedReader?) {
        var headerLine: String? = ""
        while (pReader?.readLine().also { headerLine = it }?.isNotBlank() == true) {
            val hTokens = StringTokenizer(headerLine, ":")
            var key: String
            if (hTokens.hasMoreTokens()) {
                key = hTokens.nextToken()
                if (hTokens.hasMoreTokens()) {
                    request.headers(key, hTokens.nextToken().trim())
                }
            }
        }
    }

    @Throws(IOException::class)
    fun loadParameters(request: Request, pathParameters: String?, pReader: BufferedReader?) {
        val contentType = request.headers("Content-Type")
        val payload = StringBuilder()
        while (pReader?.ready() == true) {
            payload.append(pReader.read().toChar())
        }
        if (pathParameters != null) {
            payload.append(pathParameters)
        }
        if (payload.isNotEmpty()) {
            if (contentType == null) {
                Logger.warn("No Content-Type found")
            }
            if (payload.toString().contains("Content-Disposition: form-data;")) {
                Logger.error("multipart/form-data is not implemented !")
            }
            if (contentType == "application/json") {
                request.params("body", payload.toString())
            }
            if (contentType == "application/x-www-form-urlencoded" || pathParameters != null) {
                val pTokens = StringTokenizer(payload.toString(), "&")
                while (pTokens.hasMoreTokens()) {
                    val vTokens = StringTokenizer(
                            pTokens.nextToken(), "=")
                    var key: String
                    if (vTokens.hasMoreTokens()) {
                        key = vTokens.nextToken()
                        if (vTokens.hasMoreTokens()) {
                            request.params(key, vTokens.nextToken())
                        }
                    }
                }
            }
        }
    }
}