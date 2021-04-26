package com.github.gr3gdev.jserver.test

import com.github.gr3gdev.jserver.JServer
import com.github.gr3gdev.jserver.logger.Logger
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.security.TokenClientPlugin
import com.github.gr3gdev.jserver.security.TokenServerPlugin
import com.github.gr3gdev.jserver.test.route.*
import com.github.gr3gdev.jserver.thymeleaf.ThymeleafPlugin
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*

class CypressTest

fun main() {
    Logger.changeLevel(Logger.Level.DEBUG)
    val publicFile = String(SecureRoute::class.java.getResourceAsStream("/public.txt").readAllBytes())
            .replace("\n", "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
    val spec = X509EncodedKeySpec(Base64.getDecoder().decode(publicFile))

    val server = JServer.server()
            .plugins(
                    TokenServerPlugin("demo", CypressTest::class.java.getResourceAsStream("/demo.jks"), "MyDemoP@ssw0RD".toCharArray())
                            .id("jServerDemo").issuer("GR3Gdev"),
                    TokenClientPlugin(KeyFactory.getInstance("RSA").generatePublic(spec)),
                    ThymeleafPlugin()
            )
            // REACT front
            .get("/react/{file}", ReactRoute.static("/react"))
            .get("/react", ReactRoute.get())
            // API REST
            .get("/api/persons/{id}", ApiRoute.findById())
            .get("/api/persons", ApiRoute.findAll())
            .put("/api/persons", ApiRoute.save())
            // SECURE
            .get("/secure", SecureRoute.get())
            .get("/login", LoginRoute.get())
            .post("/login", LoginRoute.post())
            // THYMELEAF
            .get("/page1", ThymeleafRoute.get())
            .get("/css/{file}", RouteListener(HttpStatus.OK, Response.File("/css/test.css", "text/css")))
    // STOP
    server.get("/stop", StopRoute.exec(server))
            .start()
}
