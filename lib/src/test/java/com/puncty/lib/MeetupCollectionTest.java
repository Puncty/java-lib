package com.puncty.lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.exceptions.NotFound;
import com.puncty.lib.exceptions.Unauthorized;
import com.puncty.lib.networking.RequesterResponse;


public class MeetupCollectionTest {
    final String MOCK_MEETUP_JSON = "{\"id\": \"12345\",\"admin\": {    \"id\": \"1234\",    \"name\": \"Dude\",    \"email_address\": \"dude@example.com\"},\"members\": [{    \"id\": \"1234\",    \"name\": \"Dude\",    \"email_address\": \"dude@example.com\"}],\"datetime\": 1234,\"location\": \"Park\"}";
    final String MOCK_JOINED_JSON = "{\"meetups\": [{\"id\": \"12345\",\"admin\": {    \"id\": \"1234\",    \"name\": \"Dude\",    \"email_address\": \"dude@example.com\"},\"members\": [{    \"id\": \"1234\",    \"name\": \"Dude\",    \"email_address\": \"dude@example.com\"}],\"datetime\": 1234,\"location\": \"Park\"}, {\"id\": \"54321\",\"admin\": {    \"id\": \"4321\",    \"name\": \"OtherDude\",    \"email_address\": \"other-dude@example.com\"}, \"members\": [{    \"id\": \"4321\",    \"name\": \"OtherDude\",    \"email_address\": \"other-dude@example.com\"}],\"datetime\": 43212,\"location\": \"City\"}]}";

    MockRequester r = new MockRequester();
    Session s = new Session(r, "1234", "test");
    MeetupCollection mc = new MeetupCollection(s);

    @Test
    public void createMeetup() throws BrokenResponse {
        r.mockPost("/meetup", 
            new RequesterResponse(200, MOCK_MEETUP_JSON)
        );

        var m = mc.create(1234, "Park");
        meetupAssertions(m);
    }

    @Test
    public void joinMeetup() throws BrokenResponse, NotFound, Unauthorized {
        r.mockPut("/meetup/12345/join", 
            new RequesterResponse(200, MOCK_MEETUP_JSON)
        );

        var m = mc.join("12345");
        meetupAssertions(m);
    }

    @Test
    public void joinedMeetups() throws BrokenResponse, NotFound, Unauthorized {
        r.mockGet("/meetup", 
            new RequesterResponse(200, MOCK_JOINED_JSON)
        );

        var m = mc.joined();
        assertEquals(m.size(), 2);
        assertEquals(m.get(0).getAdmin().getName(), "Dude");
    }

    @Test
    public void getMeetup() throws NotFound, Unauthorized, BrokenResponse {
        r.mockGet("/meetup/12345", new RequesterResponse(200, MOCK_MEETUP_JSON));

        var m = mc.get("12345");
        meetupAssertions(m);
    }

    @Test
    public void leaveMeetup() throws NotFound, Unauthorized, BrokenResponse {
        r.mockGet("/meetup/12345", new RequesterResponse(200, MOCK_MEETUP_JSON)).mockPut("/meetup/leave", new RequesterResponse(200, "ok"));
        mc.get("12345").leave();
    }

    @Test
    public void deleteMeetup() throws NotFound, Unauthorized, BrokenResponse {
        r.mockGet("/meetup/12345", new RequesterResponse(200, MOCK_MEETUP_JSON)).mockDelete("/meetup/12345", new RequesterResponse(200, "ok"));
        mc.get("12345").delete();
    }

    @Test
    public void editMeetup() {
        // TODO: add "edit meetup" tests
    }

    private void meetupAssertions(Meetup m) {
        assertEquals(m.getAdmin().getId(), "1234");
        assertEquals(m.getAdmin().getName(), "Dude");
        assertEquals(m.getAdmin().getEmail(), "dude@example.com");
        assertTrue(m.getAdmin().equals(m.getMembers().get(0)));

        assertEquals(m.getDatetime().getTime(), 1234000);
        assertEquals(m.getLocation(), "Park");    
    }

}
