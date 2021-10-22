package com.github.gr3gdev.jserver.test.route;

import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.RouteListener;

public class StopRoute {

    @RouteMapping(path = "/stop", method = RequestMethod.GET)
    public RouteListener exec() {
        // TODO
        return new RouteListener();
    }


}
