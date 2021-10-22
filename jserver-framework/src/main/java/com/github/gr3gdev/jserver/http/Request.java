package com.github.gr3gdev.jserver.http;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Interface for requests.
 *
 * @author Gregory Tardivel
 */
public interface Request {

    /**
     * Path URL.
     *
     * @return path
     */
    String path();

    /**
     * HTTP Method.
     *
     * @return method
     */
    String method();

    /**
     * HTTP Protocol.
     *
     * @return protocol
     */
    String protocol();

    /**
     * Get HTTP Request Headers.
     *
     * @return headers
     */
    Optional<String> headers(String key);

    /**
     * Put HTTP Request Headers.
     *
     * @return headers
     */
    void headers(String key, String value);

    /**
     * Get HTTP Request Headers names.
     */
    Set<String> headersNames();

    /**
     * Get HTTP Request Parameters.
     *
     * @return params
     */
    Optional<String> params(String key);

    /**
     * Get HTTP Request Parameters.
     *
     * @param keys Parameter keys
     * @param action Action to execute with parameters
     */
    void params(Stream<String> keys, Consumer<Map<String, String>> action);

    /**
     * Put HTTP Request Parameters.
     *
     * @return params
     */
    void params(String key, String value);

    /**
     * Get HTTP Request Parameters names.
     */
    Set<String> paramsNames();

    /**
     * Client remote address.
     *
     * @return remoteAddress
     */
    RemoteAddress remoteAddress();
}