package com.puncty.lib.collections;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import com.puncty.lib.Meetup;
import com.puncty.lib.Session;
import com.puncty.lib.exceptions.BrokenResponse;

public class MeetupCollection {
    private Session session;

    public MeetupCollection(Session s) {
        this.session = s;
    }

    public Meetup create(long timestamp, String location) throws BrokenResponse, JSONException {
        var path = "/meetup";

        var data = new HashMap<String, String>();
        data.put("timestamp", (timestamp / 1000)+"");
        data.put("location", location);

        var resp = this.session.post(path, data);
        return Meetup.fromJSON(this.session, resp.json.get());
    }
}
