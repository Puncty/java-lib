package com.puncty.lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import com.puncty.lib.exceptions.RequestFailed;
import com.puncty.lib.exceptions.Unauthorized;

public class User {
    boolean isOwned;
    String token = "";
    
    private String name;
    private String id;
    private String emailAddress;

    private Requester requester;

    public User(Requester requester, String name, String id, String emailAddress, boolean isOwned) {
        this.requester = requester;
        this.name = name;
        this.id = id;
        this.emailAddress = emailAddress;
        this.isOwned = isOwned;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    private void ensureOwner() throws Unauthorized {
        if (!this.isOwned) {
            throw new Unauthorized("the accessed user is not owned, and can therefore not perform this action");
        }
    }

    public void fetchToken(String password) throws Unauthorized, JSONException, IOException, InterruptedException, RequestFailed {
        ensureOwner();

        HashMap<String, String> data = new HashMap<>();

        data.put("email-address", this.emailAddress);
        data.put("password", password);

        var resp = this.requester.post("/account/login", data);
        if (resp.json.isPresent()) {
            this.token = resp.json.get().getString("token");
        } else if (resp.statusCode != 200) {
            throw new RequestFailed(resp.statusCode);
        }
    }

    public String getToken() throws Unauthorized {
        ensureOwner();

        return this.token;
    }

    public static User fromJSON(Requester requester, JSONObject data, boolean isOwned) throws JSONException {
        String name = data.getString("name");
        String id = data.getString("id");
        String emailAddress = data.getString("email-address");

        return new User(requester, name, id, emailAddress, isOwned);
    }
}
