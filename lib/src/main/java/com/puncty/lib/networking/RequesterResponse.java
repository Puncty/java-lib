package com.puncty.lib.networking;

import java.net.http.HttpResponse;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

public class RequesterResponse {
    public int statusCode;
    public Optional<JSONObject> json;
    public String text;

    public RequesterResponse(int statusCode, String text) {
        this.statusCode = statusCode;
        this.text = text;
        try {
            this.json = Optional.of(new JSONObject(this.text.strip()));
        } catch (JSONException e) {
            e.printStackTrace();
            this.json = Optional.empty();
        }
    }
}
