package com.github.gr3gdev.jserver.security.http;

import com.github.gr3gdev.jserver.http.Request;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TokenRequest.
 *
 * @author Gregory Tardivel
 */
public class TokenRequest {

    protected static final String AUTH = "Authorization";
    protected static final String COOKIES = "Cookie";

    /**
     * Get JWT token from Authorization header.
     */
    public static Optional<String> getTokenFromHeader(final Request req) {
        // Token in Authorization
        final AtomicReference<Optional<String>> token = new AtomicReference<>(Optional.empty());
        req.headers(AUTH).ifPresent(header -> {
            if (header.toLowerCase().startsWith("bearer")) {
                token.set(Optional.of(header.substring("Bearer ".length())));
            }
        });
        return token.get();
    }

    /**
     * Get JWT token from Cookie.
     */
    public static Optional<String> getTokenFromCookie(final Request req, final String cookieName) {
        // Token in cookie
        final AtomicReference<Optional<String>> token = new AtomicReference<>(Optional.empty());
        req.headers(COOKIES).ifPresent(ch -> {
            final String[] cookies = ch.split(" ");
            final String tokenCookie = Arrays.stream(cookies)
                    .filter(c -> c.startsWith(cookieName + "="))
                    .findFirst().orElse("");
            if (!tokenCookie.isBlank() && tokenCookie.contains("=")) {
                final String tokenValue = tokenCookie.split("=")[1];
                if (tokenValue.endsWith(";")) {
                    token.set(Optional.of(tokenValue.substring(0, tokenValue.length() - 1)));
                } else {
                    token.set(Optional.of(tokenValue));
                }

            }
        });
        return token.get();
    }

}
