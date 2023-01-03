package com.puncty.lib.exceptions;

/**
 * this class covers the 401 status code
 */
public class Unauthorized extends Exception {
    public Unauthorized(String errorMessage) {
        super(errorMessage);
    }
}
