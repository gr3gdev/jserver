package com.github.gr3gdev.jserver

import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.ResponseData
import com.github.gr3gdev.jserver.route.RouteListener
import org.junit.Ignore
import org.junit.Test
import java.security.SecureRandom
import java.util.stream.IntStream

@Ignore
class ServerTest {

    @Test
    fun `test Perf Server`() {
        val max = 100
        val server = Server().port(8080)
        val rand = SecureRandom()
        server.get("/test", RouteListener().process { req ->
            val res = ResponseData()
            req.params("message").ifPresent {
                val content = ByteArray(100000)
                rand.nextBytes(content)
                res.status = HttpStatus.OK
                res.content = content
                if (it == "$max") {
                    server.stop()
                }
            }
            res
        }).start()
        if (server.isAlive()) {
            IntStream.rangeClosed(1, max).forEach {
                Client().connect("localhost", 8080).send("""
                    GET /test?message=$it HTTP/1.1
                    User-Agent: Mozilla/4.0
                    Host: www.example.com
                    Accept-Language: en, fr
                    Connection: Keep-Alive

                    
                """.trimIndent())
            }
        }
    }

}