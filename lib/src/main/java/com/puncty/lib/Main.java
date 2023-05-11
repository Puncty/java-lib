package com.puncty.lib;
import java.io.IOException;

import com.puncty.lib.exceptions.BrokenResponse;
import com.puncty.lib.exceptions.UserAlreadyExists;
import com.puncty.lib.networking.Requester;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, BrokenResponse, UserAlreadyExists {
        Requester r = new Requester("https://puncty.johannespour.de");
        Session s = Session.register(r, "othertestuser", "othertestuser@gmail.com", "1234");
        UserCollection uc = new UserCollection(s);

        System.out.println(uc.getMe().getName());
    }
}
