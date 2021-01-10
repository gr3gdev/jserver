package com.github.gr3gdev.jserver.security

import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.security.user.UserData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.security.SecureRandom

class TokenExtractorTest {

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
        override fun headers(): Map<String, String> = headers
        override fun params(): Map<String, String> = HashMap()
    }

    @Before
    fun `Configure logger`() {
        Logger.changeLevel(Logger.Level.DEBUG)
    }

    @Test
    fun `Test getToken from Authorization`() {
        val tokenExtractor = TokenExtractor.secretKey("my_secret-ForTESTS")
                .id("jServer").issuer("GR3Gdev")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Authorization", "Bearer $token")))
        assertEquals(token, tokenExtractor.getToken(request))
    }

    @Test
    fun `Test getToken from Cookie`() {
        val tokenExtractor = TokenExtractor.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev")
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=A; GR3Gdev_JWT=$token; COOKIE2=B; COOKIE3=C")))
        assertEquals(token, tokenExtractor.getToken(request))
    }

    @Test
    fun `Test getUserData from Authorization`() {
        val tokenExtractor = TokenExtractor.generateSecretKey(128)
                .id("jServer").issuer("GR3Gdev")
        val userData = UserTest("Name1", 1)
        val token = tokenExtractor.createToken(userData, 60 * 60 * 1000)
        val request = RequestTest(mapOf(Pair("Authorization", "Bearer $token")))
        val user = tokenExtractor.getUserData(request, UserTest::class.java)
        assertEquals("Name1", user?.name)
        assertEquals(1, user?.count)
    }

    @Test
    fun `Test getUserData from Cookie`() {
        val tokenExtractor = TokenExtractor.generateSecretKey(256)
                .id("jServer").issuer("GR3Gdev")
        val userData = UserTest("Name2", 2)
        val token = tokenExtractor.createToken(userData, 60 * 60 * 1000)
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=A; GR3Gdev_JWT=$token; COOKIE2=B")))
        val user = tokenExtractor.getUserData(request, UserTest::class.java)
        assertEquals("Name2", user?.name)
        assertEquals(2, user?.count)
    }

}
