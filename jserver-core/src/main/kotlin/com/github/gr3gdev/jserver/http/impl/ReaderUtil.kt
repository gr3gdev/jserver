package com.github.gr3gdev.jserver.http.impl

import java.io.BufferedReader
import java.io.IOException
import java.util.*

/**
 * @author Gregory Tardivel
 */
internal object ReaderUtil {

    @Throws(IOException::class)
    fun loadHeaders(pReader: BufferedReader): Map<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        var headerLine: String
        while (pReader.readLine().also { headerLine = it }.isNotEmpty()) {
            val hTokens = StringTokenizer(headerLine, ": ")
            var key: String
            if (hTokens.hasMoreTokens()) {
                key = hTokens.nextToken()
                if (hTokens.hasMoreTokens()) {
                    headers[key] = hTokens.nextToken()
                }
            }
        }
        return headers
    }

    @Throws(IOException::class)
    fun loadParameters(
            pReader: BufferedReader): Map<String, String> {
        val parameters: MutableMap<String, String> = HashMap()
        val payload = StringBuilder()
        while (pReader.ready()) {
            payload.append(pReader.read().toChar())
        }
        if (payload.isNotEmpty()) {
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
        return parameters
    }
}