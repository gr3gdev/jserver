package com.github.gr3gdev.jserver.annotations;

import com.github.gr3gdev.jserver.http.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RouteMapping.
 *
 * @author Gregory Tardivel
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RouteMapping {

    String path();

    RequestMethod method();

}
