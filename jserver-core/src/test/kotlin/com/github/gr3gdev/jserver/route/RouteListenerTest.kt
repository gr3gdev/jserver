package com.github.gr3gdev.jserver.route

import com.github.gr3gdev.jserver.http.RemoteAddress
import com.github.gr3gdev.jserver.http.Request
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.*

class RouteListenerTest {

    private val req = object : Request {
        override fun path(): String {
            TODO("Not yet implemented")
        }

        override fun method(): String {
            TODO("Not yet implemented")
        }

        override fun protocol(): String {
            TODO("Not yet implemented")
        }

        override fun headers(key: String): Optional<String> {
            TODO("Not yet implemented")
        }

        override fun <T> headers(key: String, ifPresent: (header: String) -> T, orElse: () -> T): T {
            TODO("Not yet implemented")
        }

        override fun headers(key: String, value: String) {
            TODO("Not yet implemented")
        }

        override fun headersNames(): MutableSet<String> {
            TODO("Not yet implemented")
        }

        override fun params(key: String): Optional<String> {
            TODO("Not yet implemented")
        }

        override fun <T> params(key: String, ifPresent: (param: String) -> T, orElse: () -> T): T {
            TODO("Not yet implemented")
        }

        override fun params(key: String, value: String) {
            TODO("Not yet implemented")
        }

        override fun paramsNames(): MutableSet<String> {
            TODO("Not yet implemented")
        }

        override fun remoteAddress(): RemoteAddress {
            TODO("Not yet implemented")
        }

    }

    @Test
    fun `test run`() {
        val route = RouteListener().process {
            val res = Response()
            res.status = HttpStatus.OK
            res.content = "Test OK".toByteArray()
            res
        }
        val output = ByteArrayOutputStream()
        route.handleEvent(req, output)
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 7\r\n\r\nTest OK", String(output.toByteArray()))
    }

}
