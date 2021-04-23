package com.github.gr3gdev.jserver.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.gr3gdev.jserver.plugin.ServerPlugin
import com.github.gr3gdev.jserver.security.user.UserData
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.io.InputStream
import java.security.Key
import java.security.KeyStore
import java.util.*

class TokenServerPlugin(alias: String, keystoreFile: InputStream, keystorePassword: CharArray) : ServerPlugin {

    private val mapper = jacksonObjectMapper()
    private val key: Key
    private var id: String = UUID.randomUUID().toString()
    private var issuer = "Unknown"

    init {
        keystoreFile.use {
            val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
            keystore.load(it, keystorePassword)
            this.key = keystore.getKey(alias, keystorePassword)
        }
    }

    /**
     * Specify JWT id.
     */
    fun id(id: String): TokenServerPlugin {
        this.id = id
        return this
    }

    /**
     * Specify JWT issuer.
     */
    fun issuer(issuer: String): TokenServerPlugin {
        this.issuer = issuer
        return this
    }

    /**
     * Create a JWT from user data.
     */
    fun <T : UserData> createToken(userData: T, expirationMillis: Long): String {
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)

        // Set the JWT Claims
        val builder: JwtBuilder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(mapper.writeValueAsString(userData))
                .setIssuer(issuer)
                .signWith(SignatureAlgorithm.RS512, key)

        // Add the expiration
        if (expirationMillis > 0) {
            val expMillis: Long = nowMillis + expirationMillis
            val exp = Date(expMillis)
            builder.setExpiration(exp)
        }
        return builder.compact()
    }

}