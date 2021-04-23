package com.github.gr3gdev.jserver.test.route

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.gr3gdev.jserver.route.HttpStatus
import com.github.gr3gdev.jserver.route.Response
import com.github.gr3gdev.jserver.route.RouteListener

object ApiRoute {

    private val mapper = jacksonObjectMapper()
    private val persons = ArrayList<Person>()

    class Person {
        var id: Int = 0
        var name: String = ""
    }

    fun findAll() = RouteListener().process {
        Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(persons))
    }

    fun save() = RouteListener().process {route ->
        route.request.params("body", {
            val person = mapper.readValue(it, Person::class.java)
            if (person.id > 0) {
                // Update person
                persons.first { p -> p.id == person.id }.name = person.name
                Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(person))
            } else {
                // Create person
                persons.add(person)
                person.id = persons.size
                Response(HttpStatus.CREATED, "application/json", mapper.writeValueAsBytes(person))
            }
        }, {
            Response(HttpStatus.INTERNAL_SERVER_ERROR)
        })
    }

    fun findById() = RouteListener().process { route ->
        route.request.params("id", { id ->
            val person = persons.firstOrNull { it.id == id.toInt() }
            if (person == null) {
                Response(HttpStatus.NOT_FOUND, "application/json", "{}".toByteArray())
            } else {
                Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(person))
            }
        }, {
            Response(HttpStatus.NOT_FOUND, "application/json", "{}".toByteArray())
        })
    }
}