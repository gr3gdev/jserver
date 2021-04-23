@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package com.github.gr3gdev.jserver.security.http

import com.github.gr3gdev.jserver.http.RemoteAddress
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.security.TokenClientPlugin
import com.github.gr3gdev.jserver.security.TokenServerPlugin
import com.github.gr3gdev.jserver.security.user.UserData
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sun.security.x509.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.*
import java.security.cert.X509Certificate
import java.util.*
import kotlin.collections.HashMap


class TokenRequestTest {

    private val keystoreFile = "build/keystore.jks"
    private val keystorePassword = "Secret4Keystore"
    private lateinit var publicKey: PublicKey

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
        override fun <T> headers(key: String, ifPresent: (header: String) -> T, orElse: () -> T): T {
            var res: T? = null
            headers(key).ifPresentOrElse({
                res = ifPresent(it)
            }, {
                res = orElse()
            })
            return res!!
        }

        override fun headers(key: String, value: String) {}
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

    @Before
    fun `Configure logger`() {
        Logger.changeLevel(Logger.Level.DEBUG)
    }

    private fun generateCertificate(keyPair: KeyPair): X509Certificate {
        val privateKey = keyPair.private
        val info = X509CertInfo()
        val from = Date()
        val to = Date(from.time + 5 * 1000L * 24L * 60L * 60L)
        val interval = CertificateValidity(from, to)
        val serialNumber = BigInteger(64, SecureRandom())
        val owner = X500Name("cn=Unknown")
        var sigAlgId = AlgorithmId(AlgorithmId.MD5_oid)
        info[X509CertInfo.VALIDITY] = interval
        info[X509CertInfo.SERIAL_NUMBER] = CertificateSerialNumber(serialNumber)
        info[X509CertInfo.SUBJECT] = owner
        info[X509CertInfo.ISSUER] = owner
        info[X509CertInfo.KEY] = CertificateX509Key(keyPair.public)
        info[X509CertInfo.VERSION] = CertificateVersion(CertificateVersion.V3)
        info[X509CertInfo.ALGORITHM_ID] = CertificateAlgorithmId(sigAlgId)

        // Sign the cert to identify the algorithm that's used.
        var certificate = X509CertImpl(info)
        certificate.sign(privateKey, "SHA256withRSA")

        // Update the algorith, and resign.
        sigAlgId = certificate[X509CertImpl.SIG_ALG] as AlgorithmId
        info[CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM] = sigAlgId
        certificate = X509CertImpl(info)
        certificate.sign(privateKey, "SHA256withRSA")
        return certificate
    }

    @Before
    fun `Init keystore`() {
        val file = File(keystoreFile)
        val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(1024)
        val keyPair = keyGen.generateKeyPair()
        this.publicKey = keyPair.public

        val chain = arrayOf(generateCertificate(keyPair))

        keystore.load(null, keystorePassword.toCharArray())
        keystore.setKeyEntry("test", keyPair.private, keystorePassword.toCharArray(), chain)
        FileOutputStream(file).use {
            keystore.store(it, keystorePassword.toCharArray())
        }
    }

    @After
    fun `Delete keystore`() {
        File(keystoreFile).delete()
    }

    @Test
    fun `Test getToken from header missing`() {
        val request = RequestTest(HashMap())
        TokenRequest.getTokenFromHeader(request, {
            fail("Token present")
        }, {
            assertTrue(true)
        })
    }

    @Test
    fun `Test getToken from header Basic`() {
        val request = RequestTest(mapOf(Pair("Authorization", "Basic ABCDEFGHIJKL")))
        TokenRequest.getTokenFromHeader(request, {
            fail("Token present")
        }, {
            assertTrue(true)
        })
    }

    @Test
    fun `Test getToken from header`() {
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Authorization", "Bearer $token")))
        TokenRequest.getTokenFromHeader(request, {
            assertEquals(token, it)
        }, {
            fail("Token missing")
        })
    }

    @Test
    fun `Test getToken from Cookie 1`() {
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=A; MY_COOKIE1=$token; COOKIE2=B; COOKIE3=C")))
        TokenRequest.getTokenFromCookie(request, "MY_COOKIE1", {
            assertEquals(token, it)
        }, {
            fail("Cookie not found")
        })
    }

    @Test
    fun `Test getToken from Cookie 2`() {
        val token = SecureRandom().nextLong().toString()
        val request = RequestTest(mapOf(Pair("Cookie", "COOKIE1=ABCDEFGHIJKL; MY_COOKIE2=$token")))
        TokenRequest.getTokenFromCookie(request, "MY_COOKIE2", {
            assertEquals(token, it)
        }, {
            fail("Cookie not found")
        })
    }

    @Test
    fun `Test getUserData from token`() {
        val tokenServer = TokenServerPlugin("test", FileInputStream(keystoreFile), keystorePassword.toCharArray())
                .id("jServer").issuer("GR3Gdev")
        val tokenClient = TokenClientPlugin(publicKey)
        val userData = UserTest("Name1", 1)
        val token = tokenServer.createToken(userData, 60 * 60 * 1000)
        tokenClient.getUserData(token, UserTest::class.java, {
            val data = it.data
            assertEquals("Name1", data.name)
            assertEquals(1, data.count)
        }, {
            fail("UserData not found")
        })
    }
}
