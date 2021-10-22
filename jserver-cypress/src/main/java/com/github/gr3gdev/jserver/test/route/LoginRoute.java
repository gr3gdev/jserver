package com.github.gr3gdev.jserver.test.route;

import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.Response;
import com.github.gr3gdev.jserver.route.RouteListener;
import com.github.gr3gdev.jserver.security.TokenServerPlugin;
import com.github.gr3gdev.jserver.security.password.BCryptPasswordManager;
import com.github.gr3gdev.jserver.test.bean.User;

import java.util.stream.Stream;

public class LoginRoute {

    private final BCryptPasswordManager bCryptPasswordManager = new BCryptPasswordManager(10);
    private final User user = new User("user", bCryptPasswordManager.encode("password"));

    @RouteMapping(path = "/login", method = RequestMethod.GET)
    public RouteListener get() {
        return new RouteListener(HttpStatus.OK, "/pages/login.html", "text/html");
    }

    @RouteMapping(path = "/login", method = RequestMethod.POST)
    public RouteListener post() {
        return new RouteListener().process(route -> {
            final Response res = new Response(HttpStatus.OK);
            res.redirect("/login");
            route.getRequest().params(Stream.of("username", "password"), (values) -> {
                if (values.get("username").equals(user.getUsername())
                        && bCryptPasswordManager.matches(values.get("password"), user.getPassword())) {
                    System.out.println("User authenticated");
                    final String token = route.plugin(TokenServerPlugin.class).createToken(user, 60 * 60 * 1000L);
                    res.setStatus(HttpStatus.OK);
                    res.cookie(Response.createCookie("MY_AUTH_COOKIE", token));
                    final Response.Cookie cookie = Response.createCookie("TEST", "OK");
                    cookie.setMaxAge(600);
                    cookie.setPath("/test");
                    cookie.setSecure(true);
                    res.cookie(cookie);
                    res.redirect("/secure");
                } else {
                    System.err.println("User or password incorrect");
                }
            });
            return res;
        });
    }

}
