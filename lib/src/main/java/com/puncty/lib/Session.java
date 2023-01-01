package com.puncty.lib;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;

import com.puncty.lib.exceptions.RequestFailed;
import com.puncty.lib.exceptions.UserAlreadyExists;

public class Session {
    private Requester requester;
    private String id;
    private String token;

    public Session(Requester req, String id, String tok) {
        this.requester = req;
        this.id = id;
        this.token = tok;
    }

    public RequesterResponse get(String path) throws IOException, InterruptedException {
        var data = new HashMap<String, String>();

        return this.requester.get(path);
    }

    public static Session register(Requester req, String name, String email, String password) throws UserAlreadyExists, RequestFailed, IOException, InterruptedException, JSONException {
        var data = new HashMap<String, String>();
        data.put("name", name);
        data.put("email-address", email);
        data.put("password", password);
        
        var resp = req.get("/account/register", data);
        if (resp.statusCode == 400) {
            throw new UserAlreadyExists();
        } else if (resp.statusCode != 200 || resp.json.isEmpty()) {
            throw new RequestFailed(resp.statusCode);
        }

        var json = resp.json.get();
        return new Session(req, json.getString("id"), json.getString("token"));
    }

    public static Session login(Requester req, String email, String password) throws RequestFailed, IOException, InterruptedException, JSONException {
        var data = new HashMap<String, String>();
        data.put("email-address", email);
        data.put("password", password);

        RequesterResponse resp;
        try {
            resp = req.get("/account/login", data);        
        } catch (Exception e) {
            throw new RequestFailed(-1);
        }
        if (resp.statusCode != 200 || resp.json.isEmpty()) {
            throw new RequestFailed(resp.statusCode);
        }

        var json = resp.json.get();
        return new Session(req, json.getString("id"), json.getString("token"));
    }

}
