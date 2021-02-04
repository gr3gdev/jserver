package com.github.gr3gdev.jserver.route

import org.junit.Assert.assertEquals
import org.junit.Test

class ResponseTest {

    @Test
    fun `Test read html file`() {
        val res = Response()
        res.file(Response.File("/test/page1.html", "text/html"))
        assertEquals("Content type not match", "text/html", res.contentType)
        assertEquals("Content not match", """
            <!DOCTYPE HTML>
            <html>
            <head>
                <title>Test page1</title>
            </head>
            <body>
            <h1>Test page OK</h1>
            </body>
            </html>
        """.trimIndent(), String(res.content))
    }

    @Test
    fun `Test read js file`() {
        val res = Response()
        res.file(Response.File("/test/script.js", "application/javascript"))
        assertEquals("Content type not match", "application/javascript", res.contentType)
        assertEquals("Content not match", """
            function test() {
                console.log('This is a test !');
            }
        """.trimIndent(), String(res.content))
    }

}