package com.github.gr3gdev.jserver.route;

import com.github.gr3gdev.jserver.http.Request;
import com.github.gr3gdev.jserver.plugin.ServerPlugin;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * RouteListener.
 *
 * @author Gregory Tardivel
 */
public class RouteListener {

    private final Response responseData;
    private Function<Route, Response> run;
    private Set<ServerPlugin> plugins;

    public RouteListener() {
        responseData = new Response();
    }

    public RouteListener(HttpStatus status, String contentType, byte[] content) {
        this();
        responseData.setStatus(status);
        responseData.setContentType(contentType);
        responseData.setContent(content);
    }

    public RouteListener(HttpStatus status, String pathFile, String contentType) {
        this();
        responseData.setStatus(status);
        responseData.file(pathFile, contentType);
    }

    /**
     * Process before rendered.
     */
    public RouteListener process(Function<Route, Response> run) {
        this.run = run;
        return this;
    }

    private byte[] constructResponseHeader(Response response) {
        final List<String> headers = new ArrayList<>();
        if (response.getRedirect() != null) {
            response.setStatus(HttpStatus.FOUND);
            headers.add("Location: " + response.getRedirect());
        } else {
            headers.add("Content-Type: " + response.getContentType());
            headers.add("Content-Length: " + response.getContent().length);
        }
        response.getCookies().forEach(cookie -> headers.add("Set-Cookie: " + cookie));
        return ("HTTP/1.1 " + response.getStatus().getCode() + "\r\n"
                + String.join("\r\n", headers) + "\r\n\r\n")
                .getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Execute RouteListener.
     *
     * @param request HTTP Request
     * @param output  Output stream for HTTP response
     */
    public void handleEvent(Request request, OutputStream output) {
        Response response = this.responseData;
        if (this.run != null) {
            response = this.run.apply(new Route(request, this.plugins));
        }
        try {
            output.write(constructResponseHeader(response));
            if (response.getContent().length > 0) {
                output.write(response.getContent());
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerPlugins(Set<ServerPlugin> serverPlugins) {
        this.plugins = serverPlugins;
    }

}
