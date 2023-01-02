package com.puncty.lib.exceptions;

public class RequestFailed extends Exception {
    public RequestFailed(int statusCode) {
        super("request failed" + (
            statusCode >= 0 ? "with status " + statusCode : "due to an internal issue"
        ));
    }
}
