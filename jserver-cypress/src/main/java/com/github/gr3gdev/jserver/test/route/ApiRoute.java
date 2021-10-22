package com.github.gr3gdev.jserver.test.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.Response;
import com.github.gr3gdev.jserver.route.RouteListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApiRoute {

    private final ObjectMapper mapper = new ObjectMapper();
    private final List<Person> persons = new ArrayList<>();

    static class Person {
        private int id = 0;
        private String name = "";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @RouteMapping(path = "/api/persons", method = RequestMethod.GET)
    public RouteListener findAll() {
        return new RouteListener().process(route -> {
            try {
                return new Response(HttpStatus.OK, "application/json", mapper.writeValueAsBytes(persons));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @RouteMapping(path = "/api/persons", method = RequestMethod.PUT)
    public RouteListener save() {
        return new RouteListener().process(route -> {
            final Response res = new Response(HttpStatus.NO_CONTENT);
            route.getRequest().params("body").ifPresent(it -> {
                try {
                    final Person person = mapper.readValue(it, Person.class);
                    if (person.id > 0) {
                        // Update person
                        persons.stream().filter(p -> p.id == person.id)
                                .findFirst()
                                .ifPresent(p -> {
                                    p.name = person.name;
                                    res.setStatus(HttpStatus.OK);
                                });
                    } else {
                        // Create person
                        person.id = persons.size() + 1;
                        persons.add(person);
                        res.setStatus(HttpStatus.CREATED);
                    }
                    res.setContentType("application/json");
                    res.setContent(mapper.writeValueAsBytes(person));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            return res;
        });
    }

    @RouteMapping(path = "/api/persons/{id}", method = RequestMethod.GET)
    public RouteListener findById() {
        return new RouteListener().process(
                route -> {
                    final Response res = new Response(HttpStatus.NOT_FOUND, "application/json", "{}".getBytes(StandardCharsets.UTF_8));
                    route.getRequest().params("id").ifPresent(
                            id -> {
                                final Person person = persons.stream()
                                        .filter(p -> p.id == Integer.parseInt(id))
                                        .findFirst()
                                        .orElse(null);
                                if (person != null) {
                                    res.setStatus(HttpStatus.OK);
                                    res.setContentType("application/json");
                                    try {
                                        res.setContent(mapper.writeValueAsBytes(person));
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    return res;
                });
    }
}
