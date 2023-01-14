package com.puncty.lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.puncty.lib.networking.Requester;
import com.puncty.lib.networking.RequesterResponse;

public class MockRequester extends Requester {
    private HashMap<String, RequesterResponse> pathGETResponse = new HashMap<>();
    private HashMap<String, RequesterResponse> pathPOSTResponse = new HashMap<>();
    private HashMap<String, RequesterResponse> pathPUTResponse = new HashMap<>();
    private HashMap<String, RequesterResponse> pathDELETEResponse = new HashMap<>();

    MockRequester() {
        super("http://example.com");
    }

    public MockRequester mockGet(String path, RequesterResponse resp) {
        this.pathGETResponse.put(path, resp);
        return this;
    }

    @Override
    public RequesterResponse get(String path, Map<String, String> headers) throws IOException, InterruptedException {
        return this.get(path);
    }

    @Override
    public RequesterResponse get(String path) throws IOException, InterruptedException {
        return this.pathGETResponse.get(path);
    }

    public MockRequester mockPost(String path, RequesterResponse resp) {
        this.pathPOSTResponse.put(path, resp);
        return this;
    }

    @Override
    public RequesterResponse post(String path, Map<String, String> header, Map<String, String> data) throws IOException, InterruptedException {
        return this.pathPOSTResponse.get(path);
    }

    public MockRequester mockPut(String path, RequesterResponse resp) {
        this.pathPUTResponse.put(path, resp);
        return this;
    }

    @Override
    public RequesterResponse put(String path, Map<String, String> header, Map<String, String> data) throws IOException, InterruptedException {
        return this.pathPUTResponse.get(path);
    }

    public MockRequester mockDelete(String path, RequesterResponse resp) {
        this.pathDELETEResponse.put(path, resp);
        return this;
    }

    @Override
    public RequesterResponse delete(String path, Map<String, String> header, Map<String, String> data) throws IOException, InterruptedException {
        return this.pathDELETEResponse.get(path);
    }
}
