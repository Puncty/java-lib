package com.puncty.lib;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.exceptions.UserAlreadyExists;
import com.puncty.lib.networking.Requester;
import com.puncty.lib.networking.RequesterResponse;

/**
 * a puncty session object, used for interacting with the puncty-api
 */
public class Session {
    private Requester requester;
    private String id;
    private String token;

    public Session(Requester req, String id, String tok) {
        this.requester = req;
        this.id = id;
        this.token = tok;
    }

    /**
     * send a get request with authentication
     * @param path the api-route to call
     * @return the request result
     * @throws BrokenResponse if something unexpected goes wrong
     */
    protected RequesterResponse get(String path) throws BrokenResponse {
        try {
            return this.requester.get(path, this.getAuthHeader());
        } catch (Exception e) {
            throw new BrokenResponse("GET", path);
        }
    }

    /**
     * send a post request with authentication
     * @param path the api-route to call
     * @param data the data to send along with
     * @return the request result
     * @throws BrokenResponse if something unexpected goes wrong
     */
    protected RequesterResponse post(String path, Map<String, String> data) throws BrokenResponse {
        try {
            return this.requester.post(path, data, this.getAuthHeader());
        } catch (Exception e) {
            throw new BrokenResponse("POST", path);
        }
    }

    /**
     * send a put request with authentication
     * @param path the api-route to call
     * @param data the data to send along with
     * @return the request result
     * @throws BrokenResponse if something unexpected goes wrong
     */
    protected RequesterResponse put(String path, Map<String, String> data) throws BrokenResponse {
        try {
            return this.requester.put(path, data, this.getAuthHeader());
        } catch (Exception e) {
            throw new BrokenResponse("PUT", path);
        }
    }

    /**
     * send a delete request with authentication
     * @param path the api-route to call
     * @param data the data to send along with
     * @return the request result
     * @throws BrokenResponse if something unexpected goes wrong
     */
    protected RequesterResponse delete(String path, Map<String, String> data) throws BrokenResponse {
        try {
            return this.requester.delete(path, data, this.getAuthHeader());
        } catch (Exception e) {
            throw new BrokenResponse("DELETE", path);
        }
    }
    
    private HashMap<String, String> getAuthHeader() {
        String encodedAuth = Base64.getEncoder().encodeToString(
            (this.id + ":" + this.token).getBytes()
        );
        
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + encodedAuth);
        
        return headers;
    }


    public static Session register(Requester req, String name, String email, String password) throws UserAlreadyExists, BrokenResponse {
        var path = "/account/register";
        var data = new HashMap<String, String>();
        data.put("name", name);
        data.put("email-address", email);
        data.put("password", password);
        
        RequesterResponse resp;
        try {
            resp = req.post(path, data, new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BrokenResponse("POST", path);
        }

        if (resp.statusCode == 400) {
            throw new UserAlreadyExists();
        }

        var json = resp.json.get();
        try {
            return new Session(req, json.getString("id"), json.getString("token"));
        } catch (JSONException e) {
            throw new BrokenResponse("POST", path);
        }
    }

    public static Session login(Requester req, String email, String password) throws BrokenResponse {
        var path = "/account/login";
        var data = new HashMap<String, String>();
        data.put("email-address", email);
        data.put("password", password);

        RequesterResponse resp;
        try {
            resp = req.post("/account/login", data,  new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BrokenResponse("POST", path);
        }

        var json = resp.json.get();
        try {
            return new Session(req, json.getString("id"), json.getString("token"));
        } catch (JSONException e) {
            throw new BrokenResponse("POST", path);
        }
    }

}
