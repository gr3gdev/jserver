package com.github.gr3gdev.jserver;

import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.plugin.ServerPlugin;
import com.github.gr3gdev.jserver.route.HttpStatus;
import com.github.gr3gdev.jserver.route.RouteListener;
import com.github.gr3gdev.jserver.socket.SocketEvent;
import com.github.gr3gdev.jserver.socket.SocketReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Server.
 *
 * @author Gregory Tardivel
 */
public class Server {

    private final AtomicBoolean active = new AtomicBoolean(false);
    private final Runnable runnable;

    private ServerSocket serverSocket;
    private int port = 9000;

    private final Set<SocketEvent> socketEvents = new HashSet<>();
    private final Set<ServerPlugin> serverPlugins = new HashSet<>();
    private final List<Consumer<Server>> startupEvents = new LinkedList<>();

    public void clear() {
        this.socketEvents.clear();
        this.startupEvents.clear();
        this.serverPlugins.clear();
    }

    static class ServerRunnable implements Runnable {

        private final Server server;
        private final byte[] bannerTxt;
        private final Properties properties = new Properties();

        ServerRunnable(Server server) throws IOException {
            this.server = server;
            bannerTxt = Objects.requireNonNull(getClass().getResourceAsStream("/banner.txt")).readAllBytes();
            properties.load(getClass().getResourceAsStream("/version.properties"));
        }

        @Override
        public void run() {
            if (server.serverSocket == null) {
                try {
                    server.serverSocket = new ServerSocket(server.port);
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            }
            System.out.println(new String(bannerTxt, StandardCharsets.UTF_8));
            System.out.println("jServer (" + properties.getProperty("version") + ") started on port " + server.port);
            server.startupEvents.forEach(it -> it.accept(server));
            while (server.active.get()) {
                if (server.serverSocket != null && !server.serverSocket.isClosed()) {
                    try {
                        new Thread(new SocketReader(server.serverSocket.accept(), server.socketEvents), "jServer SocketReader").start();
                    } catch (IOException exc) {
                        if (server.active.get() && !(exc instanceof SocketException)) {
                            System.err.println("Server socket error");
                            exc.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Server() throws IOException {
        socketEvents.add(
                new SocketEvent(
                        "/favicon.ico", RequestMethod.GET,
                        new RouteListener(HttpStatus.OK, "/favicon.ico", "image/vnd.microsoft.icon")
                )
        );
        this.runnable = new ServerRunnable(this);
    }

    @Deprecated
    public Server port(int port) {
        this.port = port;
        return this;
    }

    public void start() {
        if (this.runnable != null) {
            this.active.set(true);
            new Thread(this.runnable, "jServer").start();
        }
    }

    public void stop() throws IOException {
        this.active.set(false);
        if (this.serverSocket != null) {
            this.serverSocket.close();
        }
    }

    public void onStartup(int order, Consumer<Server> event) {
        this.startupEvents.add(order, event);
    }

    /**
     * Process a Request.
     *
     * @param pPath          Path URL
     * @param pRequestMethod Method HTTP
     * @param pRouteListener Route listener
     */
    public void process(String pPath, RequestMethod pRequestMethod, RouteListener pRouteListener) {
        pRouteListener.registerPlugins(this.serverPlugins);
        this.socketEvents.add(new SocketEvent(pPath, pRequestMethod, pRouteListener));
    }

    /**
     * Add plugin.
     *
     * @param plugin Plugin
     */
    public void plugin(ServerPlugin plugin) {
        this.serverPlugins.add(plugin);
    }

}
