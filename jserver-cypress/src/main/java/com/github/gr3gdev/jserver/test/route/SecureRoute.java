package com.github.gr3gdev.jserver.test.route;

import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.Response;
import com.github.gr3gdev.jserver.route.RouteListener;
import com.github.gr3gdev.jserver.security.TokenClientPlugin;
import com.github.gr3gdev.jserver.security.http.TokenRequest;
import com.github.gr3gdev.jserver.test.bean.User;

public class SecureRoute {

    @RouteMapping(path = "/secure", method = RequestMethod.GET)
    public RouteListener get() {
        return new RouteListener()
                .process(route -> {
                    final Response res = new Response("/login");
                    TokenRequest.getTokenFromCookie(route.getRequest(), "MY_AUTH_COOKIE")
                            .map(token -> route.plugin(TokenClientPlugin.class)
                                    .getUserData(token, User.class)).ifPresent(userToken -> {
                        res.redirect(null);
                        res.setStatus(HttpStatus.OK);
                        res.file("/pages/secure.html", "text/html");
                    });
                    return res;
                });
    }

}
