package com.puncty.lib.collections;

import com.puncty.lib.Session;
import com.puncty.lib.User;
import com.puncty.lib.exceptions.BrokenResponse;

public class UserCollection {
    private Session session;

    public UserCollection(Session s) {
        this.session = s;
    }

    public User getMe() throws BrokenResponse {
        // the id "me" is internally handled and will return the user that is logged in
        return this.getUser("me");
    }

    public User getUser(String id) throws BrokenResponse {
        String path = "/user/" + id;
        var resp = this.session.get(path);

        try {
            return User.fromJSON(resp.json.get());
        } catch (Exception e) {
            throw new BrokenResponse("GET", path);
        }
    }
}
