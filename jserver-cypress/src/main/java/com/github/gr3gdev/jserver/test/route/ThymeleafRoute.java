package com.github.gr3gdev.jserver.test.route;

import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.RouteListener;
import com.github.gr3gdev.jserver.thymeleaf.ThymeleafPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ThymeleafRoute {

    @RouteMapping(path = "/page1", method = RequestMethod.GET)
    public RouteListener get() {
        return new RouteListener()
                .process(route -> {
                    final Map<String, Object> variables = new HashMap<>();
                    variables.put("welcome", "Bienvenue sur la page de test thymeleaf !");
                    return route.plugin(ThymeleafPlugin.class)
                            .process("page1", variables, Locale.FRANCE);
                });
    }

    @RouteMapping(path = "/css/{file}", method = RequestMethod.GET)
    public RouteListener css() {
        return new RouteListener(HttpStatus.OK, "/css/test.css", "text/css");
    }
}
