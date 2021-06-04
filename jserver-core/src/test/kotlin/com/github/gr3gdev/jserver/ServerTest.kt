package com.github.gr3gdev.jserver

import org.junit.Test

class ServerTest {

    @Test
    fun testStartAndStop() {
        val server = JServer.server()
        for (i in 0..10) {
            server.start()
            Thread.sleep(100)
            server.stop()
        }
    }

}