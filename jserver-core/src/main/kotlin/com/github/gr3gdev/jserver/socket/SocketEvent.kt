package com.github.gr3gdev.jserver.socket

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.RequestMethod
import com.github.gr3gdev.jserver.route.RouteListener
import java.util.regex.Pattern

/**
 * SocketEvent.
 *
 * @author Gregory Tardivel
 */
internal class SocketEvent(path: String, private val method: RequestMethod, val routeListener: RouteListener) {

    private val pathParameters = HashMap<Int, String>()
    private var patternPath = path.replace("/", "\\/")

    init {
        val pattern = Pattern.compile("\\{\\w*}")
        val matcher = pattern.matcher(path)
        var index = 1
        while (matcher.find()) {
            val paramName = path.substring(matcher.start() + 1, matcher.end() - 1)
            patternPath = patternPath.replace("{$paramName}", "(.*)")
            pathParameters[index] = paramName
            index++
        }
    }

    fun match(request: Request): Boolean {
        val matcher = Pattern.compile(patternPath).matcher(request.path())
        var matching = false
        if (matcher.find()) {
            matching = true
            if (matcher.groupCount() > 0 && pathParameters.isNotEmpty()) {
                for (i in 1..matcher.groupCount()) {
                    request.params(pathParameters[i]!!, matcher.group(i))
                }
            }
        }
        return matching && method.name.equals(request.method(), ignoreCase = true)
    }

}