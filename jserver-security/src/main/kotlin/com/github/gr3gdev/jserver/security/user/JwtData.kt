package com.github.gr3gdev.jserver.security.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Claims
import java.time.Instant

class JwtData<T : UserData>(body: Claims, clazz: Class<T>) {

    private val mapper = jacksonObjectMapper()

    val data: T
    val expiration: Instant

    init {
        data = mapper.readValue(body.subject, clazz)
        expiration = body.expiration.toInstant()
    }
}
