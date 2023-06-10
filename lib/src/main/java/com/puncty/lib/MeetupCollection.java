package com.puncty.lib;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

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
        data.put("datetime", (timestamp / 1000)+"");
        data.put("location", location);

        var resp = this.session.post(path, data);
        try {
            return Meetup.fromJSON(this.session, resp.json.get());
        } catch (JSONException e) {
            throw new BrokenResponse("POST", path);
        }
    }

    /**
     * create a new meetup
     * @param datetime when will the meetup take place
     * @param location where will the meetup take place
     * @return the new meetup
     * @throws BrokenResponse if something unexpected goes wrong
     */
    public Meetup create(Date datetime, String location) throws BrokenResponse {
        return this.create(datetime.getTime(), location);
    }

    /**
     * get a certain meetup by it's id. Only works with meetups you are a member of
     * @param id the id of the meetup
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws NotFound if there is no meetup with the given id
     * @throws Unauthorized if you are not a member of this meetup
     */
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
    
    /**
     * get all meetup ids of meetups that you are a member of 
     * @return a list of meetups ids
     * @throws BrokenResponse if something unexpected goes wrong
     */
    public List<String> joined() throws BrokenResponse {
        var path = "/meetup";
        var resp = this.session.get(path);
        ArrayList<String> meetups = new ArrayList<>();

        try {
            JSONArray meetupIds = resp.json.get().getJSONArray("meetups");
            for (int i = 0; i < meetupIds.length(); i++) {
                meetups.add(meetupIds.get(i).toString());
            }
        } catch (JSONException e) {
            throw new BrokenResponse("GET", path);
        }

        return meetups;
    }

    /**
     * join a certain meetup 
     * @param id the id of the meetup to join
     * @return the joined meetup
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws NotFound if there is no meetup with the given id
     */
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

    /**
     * leave a certain meetup. This method is equivalent to {@link Meetup#leave()}
     * @param id the id of the meetup to leave
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws NotFound if there is no meetup with the given id
     */
    public void leave(String id) throws BrokenResponse, NotFound {
        var path = String.format("/meetup/%s/leave", id);
    
        var resp = this.session.put(path, new HashMap<>());
        if (resp.statusCode == 404) {
            throw new NotFound("no meetup with id " + id + " found");
        }
    }
}
