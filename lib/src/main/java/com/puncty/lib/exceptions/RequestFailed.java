package com.puncty.lib.exceptions;

public class RequestFailed extends Exception {
    public RequestFailed(int statusCode) {
        super(String.format("request failed with status %d", statusCode));
    }
}
