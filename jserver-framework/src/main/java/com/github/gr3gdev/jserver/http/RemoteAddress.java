package com.github.gr3gdev.jserver.http;

/**
 * Interface for remote address.
 *
 * @author Gregory Tardivel
 */
public interface RemoteAddress {

    /**
     * Remote socket address IP.
     *
     * @return ip
     */
    String ip();

    /**
     * Remote socket address port.
     *
     * @return port
     */
    String port();

}