package com.puncty.lib;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.exceptions.Unauthorized;

public class Meetup {
    private Session session;

    private String id;
    private User admin;
    private List<User> members;
    private Date datetime;
    private String location;

    public Meetup(Session s, String id, User admin, List<User> members, Date datetime, String location) {
        this.session = s;
        
        this.id = id;
        this.admin = admin;
        this.members = members;
        this.datetime = datetime;
        this.location = location;
    }

    public String getId() {
        return this.id;
    }

    public User getAdmin() {
        return this.admin;
    }

    /**
     * <b>ADMIN ACTION</b>: set the new admin of meetup
     * @param id the id of the new admin, which needs to be a member of this meetup first
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws Unauthorized if you are not the admin of the meetup
     */
    public void setAdmin(String id) throws BrokenResponse, Unauthorized {
        var data = this.singleKeyMap("admin", id);
        this.edit(data);
    }

    public List<User> getMembers() {
        return this.members;
    }

    public Date getDatetime() {
        return this.datetime;
    }

    /**
     * <b>ADMIN ACTION</b>: update the datetime of this meetup
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws Unauthorized if you are not the admin of this meetup
     */
    public void setDatetime(Date date) throws BrokenResponse, Unauthorized {
        var data = this.singleKeyMap("datetime", (date.getTime() / 1000)+"");
        this.edit(data);
    }

    public String getLocation() {
        return this.location;
    }

    /**
     * <b>ADMIN ACTION</b>: update the location of this meetup
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws Unauthorized if you are not the admin of this meetup
     */
    public void setLocation(String location) throws BrokenResponse, Unauthorized {
        var data = this.singleKeyMap("location", location);
        this.edit(data);
    }

    public void refresh() throws BrokenResponse {
        var path = "/meetup/" + this.id;
        var resp = this.session.get(path);

        try {
            this.updateThisFromMeetup(Meetup.fromJSON(this.session, resp.json.get()));
        } catch (Exception e) {
            throw new BrokenResponse("GET", path);
        }
    }

    /**
     * <b>ADMIN ACTION</b>: edit this meetup
     * @param data the data to edit
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws Unauthorized if you are not the admin of this meetup
     */
    public void edit(Map<String, String> data) throws BrokenResponse, Unauthorized {
        var path = "/meetup/" + this.id;
        var resp = this.session.put(path, data);

        if (resp.statusCode == 401) {
            throw new Unauthorized("attempted to edit meetup " + this.id + ", but session user is not admin");
        }

        try {
            this.updateThisFromMeetup(Meetup.fromJSON(this.session, resp.json.get()));
        } catch (Exception e) {
            throw new BrokenResponse("PUT", path);
        }
    }

    /**
     * <b>ADMIN ACTION</b>: delete this meetup
     * @throws BrokenResponse if something unexpected goes wrong
     * @throws Unauthorized if you are not the admin of this meetup
     */
    public void delete() throws BrokenResponse, Unauthorized {
        var path = "/meetup/" + this.id;
        var resp = this.session.delete(path, new HashMap<>());

        if (resp.statusCode == 401) {
            throw new Unauthorized("attempted to delete meetup " + this.id + ", but session user is not admin");
        } else if (resp.statusCode != 200) {
            throw new BrokenResponse("DELETE", path);
        }
    } 

    /**
     * leave this meetup. This method is equivalent to {@link MeetupCollection#leave(String)}
     * @throws BrokenResponse if something unexpected goes wrong
     */
    public void leave() throws BrokenResponse {
        var path = String.format("/meetup/%s/leave", this.id);
        this.session.put(path, new HashMap<>());
    }

    private void updateThisFromMeetup(Meetup other) {
        this.admin = other.admin;
        if (!this.members.equals(other.members)) {
            this.members = other.members;
        }
        this.datetime = other.datetime;
        this.location = other.location;
    }

    private HashMap<String, String> singleKeyMap(String key, String value) {
        var m = new HashMap<String, String>();
        m.put(key, value);
        return m;
    }

    public static Meetup fromJSON(Session s, JSONObject data) throws JSONException {
        var id = data.getString("id");
        var admin = User.fromJSON(data.getJSONObject("admin"));
        
        var jsonMembers = (JSONArray) data.get("members");
        List<User> members = new ArrayList<User>();
        for (int i = 0; i < jsonMembers.length(); i++) {
            members.add(User.fromJSON(jsonMembers.getJSONObject(i)));
        }

        // `* 1000`, because conversion from seconds -> millis
        var datetime = new Date(data.getLong("datetime") * 1000);
        var location = data.getString("location");

        return new Meetup(s, id, admin, members, datetime, location);
    }
}
