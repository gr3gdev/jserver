package com.github.gr3gdev.jserver.http.impl

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class RequestImplTest {

    @Test
    fun `Test get request`() {
        BufferedReader(InputStreamReader(javaClass.getResourceAsStream("/http/get.txt"))).use {
            val req = RequestImpl("", it)
            assertEquals("/test", req.path())
            assertEquals(4, req.headersNames().size)
            assertEquals("Mozilla/4.0", req.headers("User-Agent"))
            assertEquals("www.example.com", req.headers("Host"))
            assertEquals("en, fr", req.headers("Accept-Language"))
            assertEquals("Keep-Alive", req.headers("Connection"))
            assertEquals(2, req.paramsNames().size)
            assertEquals("1", req.params("param1"))
            assertEquals("why", req.params("param2"))
        }
    }

    @Test
    fun `Test post request`() {
        BufferedReader(InputStreamReader(javaClass.getResourceAsStream("/http/post.txt"))).use {
            val req = RequestImpl("", it)
            assertEquals("/login", req.path())
            assertEquals(7, req.headersNames().size)
            assertEquals("Mozilla/4.0", req.headers("User-Agent"))
            assertEquals("www.example.com", req.headers("Host"))
            assertEquals("en, fr", req.headers("Accept-Language"))
            assertEquals("Keep-Alive", req.headers("Connection"))
            assertEquals("application/x-www-form-urlencoded", req.headers("Content-Type"))
            assertEquals("length", req.headers("Content-Length"))
            assertEquals("gzip, deflate", req.headers("Accept-Encoding"))
            assertEquals(2, req.paramsNames().size)
            assertEquals("person", req.params("username"))
            assertEquals("secret", req.params("password"))
        }
    }

}