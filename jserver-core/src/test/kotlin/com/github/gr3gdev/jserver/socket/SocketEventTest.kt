package com.github.gr3gdev.jserver.socket

import com.github.gr3gdev.jserver.http.RemoteAddress
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.http.RequestMethod
import com.github.gr3gdev.jserver.route.RouteListener
import org.junit.Assert.*
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap


class SocketEventTest {

    internal class TestRequest(private val path: String, private val method: RequestMethod) : Request {
        private val parameters = HashMap<String, String>()
        override fun path(): String {
            return path
        }

        override fun method(): String {
            return method.name
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
            return Optional.ofNullable(parameters[key])
        }

        override fun <T> params(key: String, ifPresent: (param: String) -> T, orElse: () -> T): T {
            TODO("Not yet implemented")
        }

        override fun params(key: String, value: String) {
            parameters[key] = value
        }

        override fun paramsNames(): MutableSet<String> {
            return parameters.keys
        }

        override fun remoteAddress(): RemoteAddress {
            TODO("Not yet implemented")
        }

    }

    @Test
    fun `Test not match`() {
        val socket = SocketEvent("/api/test1", RequestMethod.GET, RouteListener())
        val req1 = TestRequest("/api/test1", RequestMethod.POST)
        val req2 = TestRequest("/api/test2", RequestMethod.GET)
        val req3 = TestRequest("/api/test3", RequestMethod.DELETE)
        assertFalse("Request match", socket.match(req1))
        assertFalse("Request match", socket.match(req2))
        assertFalse("Request match", socket.match(req3))
    }

    @Test
    fun `Test match simple GET`() {
        val socket = SocketEvent("/api/users", RequestMethod.GET, RouteListener())
        val req = TestRequest("/api/users", RequestMethod.GET)
        assertTrue("Request not match", socket.match(req))
    }

    @Test
    fun `Test match simple POST`() {
        val socket = SocketEvent("/api/groups", RequestMethod.POST, RouteListener())
        val req = TestRequest("/api/groups", RequestMethod.POST)
        assertTrue("Request not match", socket.match(req))
    }

    @Test
    fun `Test match with path parameters`() {
        val socket = SocketEvent("/api/users/{userId}/groups/{groupId}", RequestMethod.GET, RouteListener())
        val req = TestRequest("/api/users/101/groups/8", RequestMethod.GET)
        assertTrue("Request not match", socket.match(req))
        assertEquals("Request parameters not found", 2, req.paramsNames().size)
        assertTrue(req.params("userId").isPresent)
        req.params("userId").ifPresent {
            assertEquals("Invalid parameter value for userId", "101", it)
        }
        req.params("groupId").ifPresent {
            assertEquals("Invalid parameter value for groupId", "8", it)
        }
    }
}
