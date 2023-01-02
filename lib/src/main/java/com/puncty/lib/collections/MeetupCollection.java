package com.puncty.lib.collections;

import java.util.HashMap;

import org.json.JSONException;

import com.puncty.lib.Meetup;
import com.puncty.lib.Session;
import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.exceptions.NotFound;
import com.puncty.lib.exceptions.Unauthorized;

public class MeetupCollection {
    private Session session;

    public MeetupCollection(Session s) {
        this.session = s;
    }

    public Meetup create(long timestamp, String location) throws BrokenResponse {
        var path = "/meetup";

        var data = new HashMap<String, String>();
        data.put("timestamp", (timestamp / 1000)+"");
        data.put("location", location);

        var resp = this.session.post(path, data);
        try {
            return Meetup.fromJSON(this.session, resp.json.get());
        } catch (JSONException e) {
            throw new BrokenResponse("POST", path);
        }
    }

    public Meetup get(String id) throws BrokenResponse, NotFound, Unauthorized {
        var path = "/meetup/" + id;

        var resp = this.session.get(path);
        if (resp.statusCode == 404) {
            throw new NotFound("no meetup with id " + id + " found");
        } else if (resp.statusCode == 401) {
            throw new Unauthorized("you are not a member of this meetup");
        }
        
        try {
            return Meetup.fromJSON(session, resp.json.get());
        } catch (JSONException e) {
            throw new BrokenResponse("GET", path);
        }
    }

    public Meetup join(String id) throws BrokenResponse, NotFound {
        var path = String.format("/meetup/%s/join", id);
    
        var resp = this.session.put(path, new HashMap<>());
        if (resp.statusCode == 404) {
            throw new NotFound("no meetup with id " + id + " found");
        }

        try {
            return Meetup.fromJSON(session, resp.json.get()); 
        } catch (JSONException e) {
            throw new BrokenResponse("PUT", path);
        }
    }

    public void leave(String id) throws BrokenResponse, NotFound {
        var path = String.format("/meetup/%s/leave", id);
    
        var resp = this.session.put(path, new HashMap<>());
        if (resp.statusCode == 404) {
            throw new NotFound("no meetup with id " + id + " found");
        }
    }
}
