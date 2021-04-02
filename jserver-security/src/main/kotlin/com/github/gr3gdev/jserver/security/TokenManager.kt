package com.github.gr3gdev.jserver.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.security.user.JwtData
import com.github.gr3gdev.jserver.security.user.UserData
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.SecureRandom
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


object TokenManager {

    private const val AUTH = "Authorization"
    private const val COOKIES = "Cookie"

    private val mapper = jacksonObjectMapper()

    private lateinit var secret: ByteArray
    private lateinit var issuer: String
    private var id: String = UUID.randomUUID().toString()

    init {
        generateSecretKey(128)
    }

    fun secretKey(key: String): TokenManager {
        this.secret = DatatypeConverter.parseBase64Binary(key)
        return this
    }

    fun generateSecretKey(length: Long): TokenManager {
        val key = SecureRandom().ints(33, 126)
                .limit(length)
                .collect({ StringBuilder() }, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
        return secretKey(key)
    }

    /**
     * Specify JWT id.
     */
    fun id(id: String): TokenManager {
        this.id = id
        return this
    }

    /**
     * Specify JWT issuer.
     */
    fun issuer(issuer: String): TokenManager {
        this.issuer = issuer
        return this
    }

    /**
     * Get JWT token from Authorization header.
     */
    fun <T> getTokenFromHeader(req: Request, ifPresent: (token: String) -> T, orElse: () -> T): T {
        // Token in Authorization
        return req.headers(AUTH, {
            if (it.startsWith("Bearer", true)) {
                ifPresent(it.substring("Bearer ".length))
            } else {
                orElse()
            }
        }, {
            orElse()
        })
    }

    /**
     * Get JWT token from Cookie.
     */
    fun <T> getTokenFromCookie(req: Request, cookieName: String, ifPresent: (token: String) -> T, orElse: () -> T): T {
        // Token in cookie
        return req.headers(COOKIES, { ch ->
            val cookies = ch.split(" ")
            val tokenCookie = cookies.find { c -> c.startsWith("$cookieName=") }.orEmpty()
            if (tokenCookie.isNotEmpty() && tokenCookie.contains("=")) {
                val tokenValue = tokenCookie.split("=")[1]
                val token = if (tokenValue.endsWith(";")) {
                    tokenValue.substring(0, tokenValue.length - 1)
                } else {
                    tokenValue
                }
                ifPresent(token)
            } else {
                orElse()
            }
        }, {
            orElse()
        })
    }

    /**
     * Create a JWT from user data.
     */
    fun <T : UserData> createToken(userData: T, expirationMillis: Long): String {
        val signatureAlgorithm = SignatureAlgorithm.HS256
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val signingKey = SecretKeySpec(secret, signatureAlgorithm.jcaName)

        // Set the JWT Claims
        val builder: JwtBuilder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(mapper.writeValueAsString(userData))
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey)

        // Add the expiration
        if (expirationMillis > 0) {
            val expMillis: Long = nowMillis + expirationMillis
            val exp = Date(expMillis)
            builder.setExpiration(exp)
        }
        return builder.compact()
    }

    /**
     * Get user data from JWT token.
     */
    fun <T : UserData, R> getUserData(token: String?, clazz: Class<T>, ifPresent: (data: JwtData<T>) -> R, orElse: () -> R): R {
        return if (token != null) {
            try {
                val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                ifPresent(JwtData(claims.body, clazz, ))
            } catch (exc: Exception) {
                Logger.error("JWT UserData error", exc)
                orElse()
            }
        } else {
            orElse()
        }
    }

}
