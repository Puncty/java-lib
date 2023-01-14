package com.puncty.lib.networking;

import java.net.http.HttpResponse;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

public class RequesterResponse {
    public int statusCode;
    public Optional<JSONObject> json;
    public String text;

    public RequesterResponse(HttpResponse<String> response) {
        this.statusCode = response.statusCode();
        this.text = response.body().toString();
        try {
            this.json = Optional.of(new JSONObject(this.text));
        } catch (JSONException e) {
            this.json = Optional.empty();
        }
    }

    public RequesterResponse(int statusCode, String text) {
        this.statusCode = statusCode;
        this.text = text;
        try {
            this.json = Optional.of(new JSONObject(this.text));
        } catch (JSONException e) {
            this.json = Optional.empty();
        }
    }
}
