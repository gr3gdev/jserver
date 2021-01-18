package com.github.gr3gdev.jserver.security

import com.github.gr3gdev.jserver.http.RemoteAddress
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.security.user.UserData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.security.SecureRandom
import java.util.*

class TokenManagerTest {

    private class UserTest : UserData {

        var name: String?
        var count: Int?

        constructor() {
            this.name = null
            this.count = null
        }

        constructor(name: String, count: Int) {
            this.name = name
            this.count = count
        }
    }

    private class RequestTest(val headers: Map<String, String>) : Request {
        override fun path(): String = ""
        override fun method(): String = "GET"
        override fun protocol(): String = "http"
        override fun headers(key: String): Optional<String> = Optional.ofNullable(headers[key])
        override fun headers(key: String, value: String) {}
        override fun headersNames(): MutableSet<String> {
            TODO("Not yet implemented")
        }

        override fun params(key: String): Optional<String> {
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

    @Before
    fun `Configure logger`() {
        Logger.changeLevel(Logger.Level.DEBUG)
    }

    @Test
    fun `Test getToken from Authorization`() {
        val tokenExtractor = TokenManager.secretKey("my_secret-ForTESTS")
                .id("jServer").issuer("GR3Gdev")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Authorization", "Bearer $token")))
        assertTrue(tokenExtractor.getTokenFromHeader(request).isPresent)
        tokenExtractor.getTokenFromHeader(request).ifPresent {
            assertEquals(token, it)
        }
    }

    @Test
    fun `Test getToken from Cookie 1`() {
        val tokenExtractor = TokenManager.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev1")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=A; MY_COOKIE1=$token; COOKIE2=B; COOKIE3=C")))
        assertTrue(tokenExtractor.getTokenFromCookie(request, "MY_COOKIE1").isPresent)
        tokenExtractor.getTokenFromCookie(request, "MY_COOKIE1").ifPresent {
            assertEquals(token, it)
        }
    }

    @Test
    fun `Test getToken from Cookie 2`() {
        val tokenExtractor = TokenManager.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev2")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=ABCDEFGHIJKL; MY_COOKIE2=$token")))
        assertTrue(tokenExtractor.getTokenFromCookie(request, "MY_COOKIE2").isPresent)
        tokenExtractor.getTokenFromCookie(request, "MY_COOKIE2").ifPresent {
            assertEquals(token, it)
        }
    }

    @Test
    fun `Test getUserData from token`() {
        val tokenExtractor = TokenManager.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev")
        val userData = UserTest("Name1", 1)
        val token = tokenExtractor.createToken(userData, 60 * 60 * 1000)
        val user = tokenExtractor.getUserData(token, UserTest::class.java)
        assertTrue(user.isPresent)
        user.ifPresent {
            assertEquals("Name1", it.name)
            assertEquals(1, it.count)
        }
    }
}
