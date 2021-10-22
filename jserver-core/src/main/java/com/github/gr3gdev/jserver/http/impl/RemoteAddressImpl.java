package com.github.gr3gdev.jserver.http.impl;

import com.github.gr3gdev.jserver.http.RemoteAddress;

/**
 * RemoteAddressImpl.
 *
 * @author Gregory Tardivel
 */
public class RemoteAddressImpl implements RemoteAddress {

    private final String ip;
    private final String port;

    public RemoteAddressImpl(String remoteAddress) {
        final String[] data = remoteAddress.substring(1).split(":");
        ip = data[0];
        port = data[1];
    }

    @Override
    public String ip() {
        return null;
    }

    @Override
    public String port() {
        return null;
    }
}
