package com.github.gr3gdev.jserver.route;

import com.github.gr3gdev.jserver.http.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RouteListenerTest {

    @Mock
    private Request req;

    @Test
    public void testRun() throws IOException {
        final RouteListener route = new RouteListener().process(r -> {
            final Response res = new Response();
            res.setStatus(HttpStatus.OK);
            res.setContent("Test OK".getBytes(StandardCharsets.UTF_8));
            return res;
        });
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        route.handleEvent(req, output);
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 7\r\n\r\nTest OK", output.toString());
    }

}
