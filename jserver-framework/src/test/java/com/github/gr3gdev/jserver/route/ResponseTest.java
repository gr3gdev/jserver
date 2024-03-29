package com.github.gr3gdev.jserver.route;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

    @Test
    public void testReadHtmlFile() {
        final Response res = new Response();
        res.file("/test/page1.html", "text/html");
        assertEquals("Content type not match", "text/html", res.getContentType());
        assertEquals("Content not match", "<!DOCTYPE HTML>\n"
                + "<html>\n"
                + "<head>\n"
                + "    <title>Test page1</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Test page OK</h1>\n"
                + "</body>\n"
                + "</html>", new String(res.getContent()));
    }

    @Test
    public void testReadJsFile() {
        final Response res = new Response();
        res.file("/test/script.js", "application/javascript");
        assertEquals("Content type not match", "application/javascript", res.getContentType());
        assertEquals("Content not match", "function test() {\n"
                + "    console.log('This is a test !');\n"
                + "}", new String(res.getContent()));
    }

}
