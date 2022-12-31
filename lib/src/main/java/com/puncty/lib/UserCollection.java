package com.puncty.lib;

import java.io.IOException;
import java.util.HashMap;

import com.puncty.lib.exceptions.RequestFailed;
import com.puncty.lib.exceptions.UserAlreadyExists;

public class UserCollection {
    private Requester requester;

    public UserCollection(Requester requester) {
        this.requester = requester;
    }

    public User register(String username, String password, String emailAddress) throws UserAlreadyExists, IOException, InterruptedException, RequestFailed {
        HashMap<String, String> data = new HashMap<>();

        data.put("name", username);
        data.put("password", password);
        data.put("email-address", emailAddress);

        var resp = this.requester.post("/account/register", data);
        if (resp.statusCode == 400) {
            throw new UserAlreadyExists();
        }
        else if (resp.statusCode != 200) {
            throw new RequestFailed(resp.statusCode);
        }

        User user;

        try {
            user = User.fromJSON(requester, resp.json.get(), true);
            user.fetchToken(password);
        } catch (Exception e) {
            throw new RequestFailed(200);
        }

        return user;
    }


}
