package com.github.gr3gdev.jserver.http.impl;

import com.github.gr3gdev.jserver.http.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 * ReaderUtil.
 *
 * @author Gregory Tardivel
 */
public class ReaderUtil {

    public static void loadHeaders(Request request, BufferedReader pReader) throws IOException {
        String headerLine = pReader.readLine();
        while (headerLine != null && !headerLine.isBlank()) {
            final StringTokenizer hTokens = new StringTokenizer(headerLine, ":");
            if (hTokens.hasMoreTokens()) {
                final String key = hTokens.nextToken();
                if (hTokens.hasMoreTokens()) {
                    request.headers(key, hTokens.nextToken().trim());
                }
            }
            headerLine = pReader.readLine();
        }
    }

    public static void loadParameters(Request request, String pathParameters, BufferedReader pReader) throws IOException {
        final Optional<String> contentType = request.headers("Content-Type");
        final StringBuilder payload = new StringBuilder();
        while (pReader.ready()) {
            payload.append((char) pReader.read());
        }
        if (pathParameters != null) {
            payload.append(pathParameters);
        }
        if (payload.length() > 0) {
            contentType.ifPresentOrElse(
                    it -> {
                        if (it.startsWith("application/json")) {
                            request.params("body", payload.toString());
                        }
                        if (it.equals("application/x-www-form-urlencoded")) {
                            extractParameters(payload, request);
                        }
                    }, () -> System.err.println("No Content-Type found")
            );
            if (pathParameters != null) {
                extractParameters(payload, request);
            }
            if (payload.toString().contains("Content-Disposition: form-data;")) {
                System.err.println("multipart/form-data is not implemented !");
            }
        }
    }

    private static void extractParameters(StringBuilder payload, Request request) {
        final StringTokenizer pTokens = new StringTokenizer(payload.toString(), "&");
        while (pTokens.hasMoreTokens()) {
            final StringTokenizer vTokens = new StringTokenizer(pTokens.nextToken(), "=");
            if (vTokens.hasMoreTokens()) {
                final String key = vTokens.nextToken();
                if (vTokens.hasMoreTokens()) {
                    request.params(key, vTokens.nextToken());
                }
            }
        }
    }
}
