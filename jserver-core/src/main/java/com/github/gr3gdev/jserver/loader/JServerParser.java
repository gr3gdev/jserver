package com.github.gr3gdev.jserver.loader;

import com.github.gr3gdev.jserver.Server;
import com.github.gr3gdev.jserver.annotations.OnStartup;
import com.github.gr3gdev.jserver.annotations.Plugin;
import com.github.gr3gdev.jserver.annotations.RouteMapping;
import com.github.gr3gdev.jserver.plugin.ServerPlugin;
import com.github.gr3gdev.jserver.route.RouteListener;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;

/**
 * JServerParser.
 *
 * @author Gregory Tardivel
 */
public class JServerParser {

    public static void parse(final Set<Class<?>> classes, final Server server) {
        classes.stream()
                .filter(c -> c.getDeclaredConstructors().length > 0 && c.getDeclaredConstructors()[0].getParameterCount() == 0)
                .filter(c -> Arrays.stream(c.getDeclaredMethods()).anyMatch(m ->
                        m.isAnnotationPresent(Plugin.class) || m.isAnnotationPresent(OnStartup.class) || m.isAnnotationPresent(RouteMapping.class)))
                .forEach(c -> {
                    try {
                        final Object instance = c.getDeclaredConstructors()[0].newInstance();
                        Arrays.stream(c.getDeclaredMethods()).forEach(m -> {
                            try {
                                // Plugins
                                if (m.isAnnotationPresent(Plugin.class)) {
                                    System.out.println("Find Plugin : " + c.getCanonicalName() + "#" + m.getName());
                                    final ServerPlugin plugin = (ServerPlugin) m.invoke(instance);
                                    server.plugin(plugin);
                                }
                                // Startup events
                                if (m.isAnnotationPresent(OnStartup.class)) {
                                    System.out.println("Find OnStartup : " + c.getCanonicalName() + "#" + m.getName());
                                    final OnStartup annotation = m.getAnnotation(OnStartup.class);
                                    server.onStartup(annotation.order(), (Consumer<Server>) m.invoke(instance));
                                }
                                // Route listener methods
                                if (m.isAnnotationPresent(RouteMapping.class)) {
                                    System.out.println("Find RouteMapping : " + c.getCanonicalName() + "#" + m.getName());
                                    final RouteMapping annotation = m.getAnnotation(RouteMapping.class);
                                    server.process(annotation.path(), annotation.method(), (RouteListener) m.invoke(instance));
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

}
