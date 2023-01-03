package com.puncty.lib.exceptions;

/**
 * this class covers the 404 status code
 */
public class NotFound extends Exception {
    public NotFound(String errorMessage) {
        super(errorMessage);
    }
}
