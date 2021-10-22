package com.github.gr3gdev.jserver;

import org.junit.Test;

import java.io.IOException;

public class ServerTest {

    @Test
    public void testStartAndStop() throws IOException, InterruptedException {
        final Server server = new Server();
        for (int i = 0; i < 10; i++) {
            server.start();
            Thread.sleep(100);
            server.stop();
        }
    }

}
