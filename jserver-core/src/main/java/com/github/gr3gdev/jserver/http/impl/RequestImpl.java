package com.github.gr3gdev.jserver.http.impl;

import com.github.gr3gdev.jserver.http.RemoteAddress;
import com.github.gr3gdev.jserver.http.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RequestImpl.
 *
 * @author Gregory Tardivel
 */
public class RequestImpl implements Request {

    private final String remoteAddress;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> parameters = new HashMap<>();
    private String httpMethod = "";
    private String path = "";
    private String protocol = "";


    public RequestImpl(String remoteAddress, InputStream input) throws IOException {
        this.remoteAddress = remoteAddress;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final String requestLine = reader.readLine();
        if (requestLine != null) {
            final StringTokenizer tokens = new StringTokenizer(requestLine);
            // HTTP Method (GET, POST, ...)
            httpMethod = tokens.nextToken();
            if (tokens.hasMoreTokens()) { // Path
                path = tokens.nextToken();
            }
            if (tokens.hasMoreTokens()) { // Protocol HTTP
                protocol = tokens.nextToken();
            }
        }
        // HEADERS
        ReaderUtil.loadHeaders(this, reader);
        // HTTP PARAMETERS
        String pathParameters = null;
        if (path.contains("?")) {
            pathParameters = path.substring(path.indexOf("?") + 1);
            path = path.substring(0, path.indexOf("?"));
        }
        ReaderUtil.loadParameters(this, pathParameters, reader);
    }

    @Override
    public String path() {
        return this.path;
    }

    @Override
    public String method() {
        return this.httpMethod;
    }

    @Override
    public String protocol() {
        return this.protocol;
    }

    @Override
    public Optional<String> headers(String key) {
        return Optional.ofNullable(this.headers.get(key.toLowerCase()));
    }

    @Override
    public void headers(String key, String value) {
        this.headers.put(key.toLowerCase(), value);
    }

    @Override
    public Set<String> headersNames() {
        return this.headers.keySet();
    }

    @Override
    public Optional<String> params(String key) {
        return Optional.ofNullable(this.parameters.get(key.toLowerCase()));
    }

    @Override
    public void params(Stream<String> keys, Consumer<Map<String, String>> action) {
        final Map<String, String> values = keys.collect(Collectors.toMap(Function.identity(), k -> params(k).orElse("")));
        action.accept(values);
    }

    @Override
    public void params(String key, String value) {
        this.parameters.put(key.toLowerCase(), value);
    }

    @Override
    public Set<String> paramsNames() {
        return this.parameters.keySet();
    }

    @Override
    public RemoteAddress remoteAddress() {
        return new RemoteAddressImpl(this.remoteAddress);
    }

}
