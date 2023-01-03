package com.puncty.lib;

import com.puncty.lib.exceptions.BrokenResponse;

public class UserCollection {
    private Session session;

    public UserCollection(Session s) {
        this.session = s;
    }

    /**
     * get yourself as a user object
     * @return the user object
     * @throws BrokenResponse if something unexpected goes wrong
     */
    public User getMe() throws BrokenResponse {
        // the id "me" is internally handled and will return the user that is logged in
        return this.getUser("me");
    }

    /**
     * get a certain user
     * @param id the id of the user
     * @return the user object
     * @throws BrokenResponse if something unexpected goes wrong
     */
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
