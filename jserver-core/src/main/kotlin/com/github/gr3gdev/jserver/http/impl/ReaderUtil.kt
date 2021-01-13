package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.logger.Logger
import java.io.BufferedReader
import java.io.IOException
import java.util.*

/**
 * @author Gregory Tardivel
 */
internal object ReaderUtil {

    @Throws(IOException::class)
    fun loadHeaders(pReader: BufferedReader?): Map<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        var headerLine: String? = ""
        while (pReader?.readLine().also { headerLine = it }?.isNotBlank() == true) {
            val hTokens = StringTokenizer(headerLine, ":")
            var key: String
            if (hTokens.hasMoreTokens()) {
                key = hTokens.nextToken()
                if (hTokens.hasMoreTokens()) {
                    headers[key] = hTokens.nextToken().trim()
                }
            }
        }
        Logger.debug("HEADERS: $headers")
        return headers
    }

    @Throws(IOException::class)
    fun loadParameters(pathParameters: String?, pReader: BufferedReader?): Map<String, String> {
        val parameters: MutableMap<String, String> = HashMap()
        val payload = StringBuilder()
        while (pReader?.ready() == true) {
            payload.append(pReader.read().toChar())
        }
        if (pathParameters != null) {
            payload.append(pathParameters)
        }
        if (payload.isNotEmpty()) {
            if (payload.toString().contains("Content-Disposition: form-data;")) {
                Logger.error("multipart/form-data is not implemented !")
            }
            val pTokens = StringTokenizer(payload.toString(), "&")
            while (pTokens.hasMoreTokens()) {
                val vTokens = StringTokenizer(
                        pTokens.nextToken(), "=")
                var key: String
                if (vTokens.hasMoreTokens()) {
                    key = vTokens.nextToken()
                    if (vTokens.hasMoreTokens()) {
                        parameters[key] = vTokens.nextToken()
                    }
                }
            }
        }
        Logger.debug("PARAMETERS: $parameters")
        return parameters
    }
}