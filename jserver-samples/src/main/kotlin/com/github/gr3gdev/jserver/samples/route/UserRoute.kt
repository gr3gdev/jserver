package com.github.gr3gdev.jserver.samples.route

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.samples.bean.User

object UserRoute {

    private val mapper = jacksonObjectMapper()
    private val users = ArrayList<User>()

    fun get(): RouteListener {
        return RouteListener().process {
            Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(users))
        }
    }

    fun save(): RouteListener {
        return RouteListener().process { req ->
            req.params("body", {
                // Execute something
                val user = mapper.readValue(it, User::class.java)
                users.add(user)
                Response(HttpStatus.OK, "application/json", it.toByteArray())
            }, {
                Response(HttpStatus.NOT_FOUND, "application/json", "{}".toByteArray())
            })
        }
    }
}