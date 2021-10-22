package com.github.gr3gdev.jserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gr3gdev.jserver.plugin.ServerPlugin;
import com.github.gr3gdev.jserver.security.user.UserData;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSASigner;

import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * TokenServerPlugin.
 *
 * @author Gregory Tardivel
 */
public class TokenServerPlugin implements ServerPlugin {

    private PrivateKey key;
    private String id = UUID.randomUUID().toString();
    private String issuer = "Unknown";

    public TokenServerPlugin(final PrivateKey privateKey) {
        this.key = privateKey;
    }

    /**
     * Specify JWT id.
     *
     * @param id JWT id
     * @return TokenServerPlugin
     */
    public TokenServerPlugin id(final String id) {
        this.id = id;
        return this;
    }

    /**
     * Specify JWT issuer.
     *
     * @param issuer JWT issuer
     * @return TokenServerPlugin
     */
    public TokenServerPlugin issuer(final String issuer) {
        this.issuer = issuer;
        return this;
    }

    /**
     * Create a JWT from user data.
     */
    public <T extends UserData> String createToken(T userData, Long expirationMillis) {
        final Signer signer = RSASigner.newSHA256Signer(this.key);
        final ZonedDateTime now = ZonedDateTime.now();

        final ObjectMapper mapper = new ObjectMapper();
        // Set the JWT Claims
        try {
            final JWT jwt = new JWT()
                    .setUniqueId(this.id)
                    .setIssuer(this.issuer)
                    .setIssuedAt(now)
                    .setSubject(mapper.writeValueAsString(userData));

            // Add the expiration
            if (expirationMillis > 0) {
                jwt.setExpiration(now.plus(expirationMillis, ChronoUnit.MILLIS));
            }
            return JWT.getEncoder().encode(jwt, signer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
