package com.github.gr3gdev.jserver.samples.route

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.RouteListener
import com.github.gr3gdev.jserver.samples.bean.User

object UserRoute {

    private val mapper = jacksonObjectMapper()
    private val users = ArrayList<User>()

    fun get():RouteListener {
        return RouteListener().process { req, res ->
            res.status = HttpStatus.OK
            res.contentType = "application/json"
            res.content = mapper.writeValueAsBytes(users)
        }
    }

    fun save(): RouteListener {
        return RouteListener().process { req, res ->
            req.params("body").ifPresentOrElse({
                // Execute something
                val user = mapper.readValue(it, User::class.java)
                users.add(user)
                res.status = HttpStatus.OK
                res.contentType = "application/json"
                res.content = it.toByteArray()
            }, {
                res.status = HttpStatus.NOT_FOUND
                res.contentType = "application/json"
                res.content = "{}".toByteArray()
            })
        }
    }
}