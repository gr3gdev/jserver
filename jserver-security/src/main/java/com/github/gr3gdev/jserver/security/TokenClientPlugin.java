package com.github.gr3gdev.jserver.security;

import com.github.gr3gdev.jserver.plugin.ServerPlugin;
import com.github.gr3gdev.jserver.security.user.JwtData;
import com.github.gr3gdev.jserver.security.user.UserData;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSAVerifier;

import java.security.PublicKey;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TokenClientPlugin.
 *
 * @author Gregory Tardivel
 */
public class TokenClientPlugin implements ServerPlugin {

    private static final Logger LOGGER = Logger.getLogger(TokenClientPlugin.class.getSimpleName());

    private final PublicKey key;

    public TokenClientPlugin(final PublicKey key) {
        this.key = key;
    }

    public <T extends UserData> Optional<JwtData<T>> getUserData(final String token, final Class<T> clazz) {
        if (token != null) {
            try {
                final Verifier verifier = RSAVerifier.newVerifier(this.key);
                final JWT jwt = JWT.getDecoder().decode(token, verifier);
                return Optional.of(new JwtData<>(jwt, clazz));
            } catch (final Exception exc) {
                LOGGER.log(Level.SEVERE, "JWT UserData error", exc);
            }
        }
        return Optional.empty();
    }
}
