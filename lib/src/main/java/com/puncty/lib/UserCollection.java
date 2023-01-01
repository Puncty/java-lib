package com.puncty.lib;

import java.io.IOException;
import java.util.HashMap;

import com.puncty.lib.exceptions.RequestFailed;
import com.puncty.lib.exceptions.UserAlreadyExists;

public class UserCollection {
    private Session session;

    public UserCollection(Session s) {
        this.session = s;
    }

    public User getMe() {
    }
}
