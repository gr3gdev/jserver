package com.github.gr3gdev.jserver.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.gr3gdev.jserver.http.Request
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.ResponseData
import com.github.gr3gdev.jserver.security.user.UserData
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.SecureRandom
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


object TokenExtractor {

    private const val AUTH = "Authorization"
    private const val COOKIES = "Cookie"

    private val mapper = jacksonObjectMapper()

    private lateinit var secret: ByteArray
    private lateinit var id: String
    private lateinit var issuer: String

    fun secretKey(key: String): TokenExtractor {
        this.secret = DatatypeConverter.parseBase64Binary(key)
        return this
    }

    fun generateSecretKey(length: Long): TokenExtractor {
        val key = SecureRandom().ints(33, 126)
                .limit(length)
                .collect({ StringBuilder() }, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
        return secretKey(key)
    }

    fun id(id: String): TokenExtractor {
        this.id = id
        return this
    }

    fun issuer(issuer: String): TokenExtractor {
        this.issuer = issuer
        return this
    }

    private fun cookieName() = "${issuer}_JWT"

    fun addCookie(response: ResponseData, token: String) {
        response.cookies[cookieName()] = token
    }

    fun getToken(req: Request): String? {
        // Token in Authorization
        var token = req.headers()[AUTH]?.substring("Bearer ".length)
        if (token == null) {
            // Token in cookie
            val cookies = req.headers()[COOKIES]?.split(" ")
            val tokenCookie = cookies?.filter { it.startsWith("${cookieName()}=") }
            if (tokenCookie != null && tokenCookie.isNotEmpty()) {
                token = tokenCookie[0].split("=")[1]
                if (token.endsWith(";")) {
                    token = token.substring(0, token.length - 1)
                }
            }
        }
        Logger.debug("TOKEN: $token")
        return token
    }

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

    fun <T : UserData> getUserData(req: Request, clazz: Class<T>): T? {
        val token = getToken(req)
        return if (token != null) {
            val claims = try {
                Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
            } catch (exc: Exception) {
                Logger.error("${exc.message}\n${exc.stackTrace.joinToString("\n")}")
                null
            }
            if (claims?.body?.subject != null) {
                mapper.readValue(claims.body.subject, clazz)
            } else {
                null
            }
        } else {
            null
        }
    }

}
