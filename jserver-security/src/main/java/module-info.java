/**
 * JServer security module
 */
module jserver.security {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires org.bouncycastle.provider;
    requires jserver.framework;
    requires io.fusionauth;

    exports com.github.gr3gdev.jserver.security;
    exports com.github.gr3gdev.jserver.security.user;
    exports com.github.gr3gdev.jserver.security.password;
    exports com.github.gr3gdev.jserver.security.http;
}