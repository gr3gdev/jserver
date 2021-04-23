package com.github.gr3gdev.jserver.test.bean

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.gr3gdev.jserver.security.user.UserData

class User(): UserData {

    var username: String = ""
    @JsonIgnore
    var password: String = ""

    constructor(username: String, password: String) : this() {
        this.username = username
        this.password = password
    }

    override fun toString(): String {
        return "User(username='$username', password='$password')"
    }

}