package com.puncty.lib.exceptions;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists() {
        super("the user you tried to register does already exist");
    }
}
