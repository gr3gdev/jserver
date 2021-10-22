package com.github.gr3gdev.jserver.test.route;

import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.Response;
import com.github.gr3gdev.jserver.route.RouteListener;

import java.util.HashMap;
import java.util.Map;

public class ReactRoute {

    private final Map<String, String> mimeTypes = new HashMap<>() {{
        put(".html", "test/html");
        put(".js", "application/javascript");
        put(".css", "test/css");
        put(".png", "image/png");
        put(".json", "application/json");
        put(".map", "application/json");
        put(".svg", "image/svg+xml");
        put(".txt", "text/plain");
        put(".ico", "image/x-icon");
    }};

    @RouteMapping(path = "/react", method = RequestMethod.GET)
    public RouteListener get() {
        return new RouteListener()
                .process((route) ->
                        new Response(HttpStatus.OK, "/react/index.html", "text/html"));
    }

    @RouteMapping(path = "/react/{file}", method = RequestMethod.GET)
    public RouteListener staticFile() {
        final String path = "react";
        return new RouteListener()
                .process(route -> {
                    final Response res = new Response(HttpStatus.NOT_FOUND);
                    route.getRequest().params("file").ifPresent(file -> {
                        final String contentType = mimeTypes.entrySet().stream().filter(it -> file.endsWith(it.getKey()))
                                .findFirst()
                                .map(Map.Entry::getValue)
                                .orElse("text/plain");
                        res.setStatus(HttpStatus.OK);
                        res.file(path + "/" + file, contentType);
                    });
                    return res;
                });
    }


}
