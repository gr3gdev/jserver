package com.github.gr3gdev.jserver.http.impl

import com.github.gr3gdev.jserver.http.RemoteAddress

class RemoteAddressImpl(private val remoteAddress: String) : RemoteAddress {

    private val ip: String
    private val port: String

    init {
        val data = remoteAddress.substring(1).split(":")
        ip = data[0]
        port = data[1]
    }

    override fun ip(): String {
        return ip
    }

    override fun port(): String {
        return port
    }
}