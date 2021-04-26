package com.github.gr3gdev.jserver.test.route

import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.thymeleaf.ThymeleafPlugin
import java.util.*

object ThymeleafRoute {

    fun get() = RouteListener().process { route ->
        route.plugin(ThymeleafPlugin::class.java)
                .process(
                        "page1",
                        mapOf("welcome" to "Bienvenue sur la page de test thymeleaf !"),
                        Locale.FRANCE
                )
    }

}