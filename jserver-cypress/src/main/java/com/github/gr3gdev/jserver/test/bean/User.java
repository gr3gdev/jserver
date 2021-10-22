package com.github.gr3gdev.jserver.test.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.gr3gdev.jserver.security.user.UserData;

public class User implements UserData {

    @JsonProperty
    private String username;
    @JsonIgnore
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
