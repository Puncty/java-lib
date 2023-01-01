package com.puncty.lib;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private String id;
    private String email;

    public User(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public static User fromJSON(JSONObject data, boolean isOwned) throws JSONException {
        String name = data.getString("name");
        String id = data.getString("id");
        String email = data.getString("email-address");

        return new User(name, id, email);
    }
}
