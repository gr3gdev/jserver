package com.github.gr3gdev.jserver.http.impl;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestImplTest {

    private void validate(Optional<String> key, String value) {
        assertTrue(key.isPresent());
        key.ifPresent(it -> assertEquals(value, it));
    }

    @Test
    public void testGetRequest() throws IOException {
        try (InputStream it = RequestImplTest.class.getResourceAsStream("/http/get.txt")) {
            final RequestImpl req = new RequestImpl("", it);
            assertEquals("/test", req.path());
            assertEquals(4, req.headersNames().size());
            validate(req.headers("User-Agent"), "Mozilla/4.0");
            validate(req.headers("Host"), "www.example.com");
            validate(req.headers("Accept-Language"), "en, fr");
            validate(req.headers("Connection"), "Keep-Alive");
            assertEquals(2, req.paramsNames().size());
            validate(req.params("param1"), "1");
            validate(req.params("param2"), "why");
        }
    }

    @Test
    public void testPostRequest() throws IOException {
        try (InputStream it = RequestImplTest.class.getResourceAsStream("/http/post.txt")) {
            final RequestImpl req = new RequestImpl("", it);
            assertEquals("/login", req.path());
            assertEquals(7, req.headersNames().size());
            validate(req.headers("User-Agent"), "Mozilla/4.0");
            validate(req.headers("Host"), "www.example.com");
            validate(req.headers("Accept-Language"), "en, fr");
            validate(req.headers("Connection"), "Keep-Alive");
            validate(req.headers("Content-Type"), "application/x-www-form-urlencoded");
            validate(req.headers("Content-Length"), "length");
            validate(req.headers("Accept-Encoding"), "gzip, deflate");
            assertEquals(2, req.paramsNames().size());
            validate(req.params("username"), "person");
            validate(req.params("password"), "secret");
        }
    }

}
