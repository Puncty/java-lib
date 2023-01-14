package com.puncty.lib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.networking.RequesterResponse;

public class UserCollectionTest {
    MockRequester r = new MockRequester();
    Session s = new Session(r, "1234", "test");
    UserCollection uc = new UserCollection(s);

    @Test
    public void getMe() throws BrokenResponse {
        r.mockGet("/user/me", new RequesterResponse(
            200, 
            "{\"name\": \"Dude\", \"id\": \"1234\", \"email_address\": \"dude@example.com\"}"));    
        
        var u = uc.getMe();
        assertEquals(u.getName(), "Dude");
        assertEquals(u.getId(), "1234");
        assertEquals(u.getEmail(), "dude@example.com");
    }

    @Test
    public void getUser() throws BrokenResponse {
        r.mockGet("/user/4321", new RequesterResponse(
            200, 
            "{\"name\": \"Person\", \"id\": \"4321\", \"email_address\": \"person@example.com\"}"));    
        
        var u = uc.getUser("4321");
        assertEquals(u.getName(), "Person");
        assertEquals(u.getId(), "4321");
        assertEquals(u.getEmail(), "person@example.com");
    }
}
