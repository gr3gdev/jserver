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
        var res = Response(HttpStatus.INTERNAL_SERVER_ERROR)
        route.request.params("body").ifPresent {
            val person = mapper.readValue(it, Person::class.java)
            if (person.id > 0) {
                // Update person
                persons.first { p -> p.id == person.id }.name = person.name
                res = Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(person))
            } else {
                // Create person
                persons.add(person)
                person.id = persons.size
                res = Response(HttpStatus.CREATED, "application/json", mapper.writeValueAsBytes(person))
            }
        }
        res
    }

    fun findById() = RouteListener().process { route ->
        var res = Response(HttpStatus.NOT_FOUND, "application/json", "{}".toByteArray())
        route.request.params("id").ifPresent { id ->
            val person = persons.firstOrNull { it.id == id.toInt() }
            if (person != null) {
                res = Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(person))
            }
        }
        res
    }
}