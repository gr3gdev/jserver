package com.github.gr3gdev.jserver.security

import com.github.gr3gdev.jserver.http.RemoteAddress
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.security.user.UserData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.security.SecureRandom
import java.util.*
import kotlin.collections.HashMap

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
        override fun <T> headers(key: String, ifPresent: (header: String) -> T, orElse: () -> T) {
            headers(key).ifPresentOrElse({
                ifPresent(it)
            }, {
                orElse()
            })
        }

        override fun headers(key: String, value: String) {}
        override fun headersNames(): MutableSet<String> {
            TODO("Not yet implemented")
        }

        override fun params(key: String): Optional<String> {
            TODO("Not yet implemented")
        }

        override fun <T> params(key: String, ifPresent: (param: String) -> T, orElse: () -> T) {
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
    fun `Test getToken from header missing`() {
        val tokenExtractor = TokenManager.secretKey("my_secret-ForTESTS")
                .id("jServer").issuer("GR3Gdev")
        val request = RequestTest(HashMap())
        tokenExtractor.getTokenFromHeader(request, {
            fail("Token present")
        }, {
            assertTrue(true)
        })
    }

    @Test
    fun `Test getToken from header Basic`() {
        val tokenExtractor = TokenManager.secretKey("my_secret-ForTESTS")
                .id("jServer").issuer("GR3Gdev")
        val request = RequestTest(mapOf(Pair("Authorization", "Basic ABCDEFGHIJKL")))
        tokenExtractor.getTokenFromHeader(request, {
            fail("Token present")
        }, {
            assertTrue(true)
        })
    }

    @Test
    fun `Test getToken from header`() {
        val tokenExtractor = TokenManager.secretKey("my_secret-ForTESTS")
                .id("jServer").issuer("GR3Gdev")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Authorization", "Bearer $token")))
        tokenExtractor.getTokenFromHeader(request, {
            assertEquals(token, it)
        }, {
            fail("Token missing")
        })
    }

    @Test
    fun `Test getToken from Cookie 1`() {
        val tokenExtractor = TokenManager.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev1")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=A; MY_COOKIE1=$token; COOKIE2=B; COOKIE3=C")))
        tokenExtractor.getTokenFromCookie(request, "MY_COOKIE1", {
            assertEquals(token, it)
        }, {
            fail("Cookie not found")
        })
    }

    @Test
    fun `Test getToken from Cookie 2`() {
        val tokenExtractor = TokenManager.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev2")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=ABCDEFGHIJKL; MY_COOKIE2=$token")))
        tokenExtractor.getTokenFromCookie(request, "MY_COOKIE2", {
            assertEquals(token, it)
        }, {
            fail("Cookie not found")
        })
    }

    @Test
    fun `Test getUserData from token`() {
        val tokenExtractor = TokenManager.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev")
        val userData = UserTest("Name1", 1)
        val token = tokenExtractor.createToken(userData, 60 * 60 * 1000)
        tokenExtractor.getUserData(token, UserTest::class.java, {
            val data = it.data
            assertEquals("Name1", data.name)
            assertEquals(1, data.count)
        }, {
            fail("UserData not found")
        })
    }
}
