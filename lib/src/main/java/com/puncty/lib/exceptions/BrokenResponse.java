package com.puncty.lib.exceptions;

/**
 * this class coveres every request/response error that may occur
 */
public class BrokenResponse extends Exception {
    public BrokenResponse(String method, String path) {
        super(String.format("the response of %s %s seems to be corrupt", method, path));
    }
}
