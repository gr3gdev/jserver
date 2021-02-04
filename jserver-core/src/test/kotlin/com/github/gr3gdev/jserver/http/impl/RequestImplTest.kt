package com.github.gr3gdev.jserver.http.impl

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class RequestImplTest {

    private fun validate(key: Optional<String>, value: String) {
        assertTrue(key.isPresent)
        key.ifPresent {
            assertEquals(value, it)
        }
    }

    @Test
    fun `Test get request`() {
        javaClass.getResourceAsStream("/http/get.txt").use {
            val req = RequestImpl("", it)
            assertEquals("/test", req.path())
            assertEquals(4, req.headersNames().size)
            validate(req.headers("User-Agent"), "Mozilla/4.0")
            validate(req.headers("Host"), "www.example.com")
            validate(req.headers("Accept-Language"), "en, fr")
            validate(req.headers("Connection"), "Keep-Alive")
            assertEquals(2, req.paramsNames().size)
            validate(req.params("param1"), "1")
            validate(req.params("param2"), "why")
        }
    }

    @Test
    fun `Test post request`() {
        javaClass.getResourceAsStream("/http/post.txt").use {
            val req = RequestImpl("", it)
            assertEquals("/login", req.path())
            assertEquals(7, req.headersNames().size)
            validate(req.headers("User-Agent"), "Mozilla/4.0")
            validate(req.headers("Host"), "www.example.com")
            validate(req.headers("Accept-Language"), "en, fr")
            validate(req.headers("Connection"), "Keep-Alive")
            validate(req.headers("Content-Type"), "application/x-www-form-urlencoded")
            validate(req.headers("Content-Length"), "length")
            validate(req.headers("Accept-Encoding"), "gzip, deflate")
            assertEquals(2, req.paramsNames().size)
            validate(req.params("username"), "person")
            validate(req.params("password"), "secret")
        }
    }

}