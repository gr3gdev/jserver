package com.github.gr3gdev.jserver.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * JServerClassLoader.
 *
 * @author Gregory Tardivel
 */
public class JServerClassLoader extends URLClassLoader {

    public JServerClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            try {
                if (getSystemClassLoader() != null) {
                    loadedClass = getSystemClassLoader().loadClass(name);
                }
            } catch (ClassNotFoundException e) {
                // skip
            }
            try {
                if (loadedClass == null) {
                    loadedClass = findClass(name);
                }
            } catch (ClassNotFoundException e) {
                loadedClass = super.loadClass(name, resolve);
            }
        }
        if (resolve) {
            resolveClass(loadedClass);
        }
        return loadedClass;
    }

    @Override
    public URL getResource(String name) {
        URL res = null;
        if (getSystemClassLoader() != null) {
            res = getSystemClassLoader().getResource(name);
        }
        if (res == null) {
            res = findResource(name);
        }
        if (res == null) {
            res = super.getResource(name);
        }
        return res;
    }

    @Override
    public Enumeration<URL> getResources(String name) {
        final LinkedList<URL> allRes = new LinkedList<>();

        try {
            final Enumeration<URL> sysResources = getSystemClassLoader().getResources(name);
            if (sysResources != null) {
                while (sysResources.hasMoreElements()) {
                    allRes.add(sysResources.nextElement());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            final Enumeration<URL> thisRes = findResources(name);
            if (thisRes != null) {
                while (thisRes.hasMoreElements()) {
                    allRes.add(thisRes.nextElement());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            final Enumeration<URL> parentRes = super.findResources(name);
            if (parentRes != null) {
                while (parentRes.hasMoreElements()) {
                    allRes.add(parentRes.nextElement());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Enumeration<>() {

            private final Iterator<URL> it = allRes.iterator();

            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public URL nextElement() {
                return it.next();
            }
        };
    }
}
