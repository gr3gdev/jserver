package com.github.gr3gdev.jserver.security.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fusionauth.jwt.domain.JWT;

import java.time.Instant;

/**
 * JwtData.
 *
 * @author Gregory Tardivel
 */
public class JwtData<T extends UserData> {

    private final T data;
    private final Instant expiration;

    public JwtData(final JWT jwt, final Class<T> clazz) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        this.data = mapper.readValue(jwt.subject, clazz);
        this.expiration = jwt.expiration.toInstant();
    }

    public T getData() {
        return data;
    }

    public Instant getExpiration() {
        return expiration;
    }
}
