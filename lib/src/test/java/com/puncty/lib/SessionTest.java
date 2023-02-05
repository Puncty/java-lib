package com.puncty.lib;

import org.junit.Test;

import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.exceptions.UserAlreadyExists;
import com.puncty.lib.networking.RequesterResponse;

public class SessionTest {
    @Test
    public void register() throws UserAlreadyExists, BrokenResponse {
        var r = new MockRequester();
        r.mockPost("/account/register", new RequesterResponse(200, "{\"id\": \"1234\", \"token\": \"test\"}"));

        Session.register(r, "Dude", "dude@example.com", "p4ssw0rd");
    }

    @Test
    public void login() throws UserAlreadyExists, BrokenResponse {
        var r = new MockRequester();
        r.mockPost("/account/login", new RequesterResponse(200, "{\"id\": \"1234\", \"token\": \"test\"}"));

        Session.login(r, "dude@example.com", "p4ssw0rd");
    }
}
