package com.puncty.lib.exceptions;

public class Unauthorized extends Exception {
    public Unauthorized(String errorMessage) {
        super(errorMessage);
    }
}
