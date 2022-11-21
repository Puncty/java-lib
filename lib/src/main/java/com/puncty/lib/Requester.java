package com.puncty.lib;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Requester {
    private URI baseUrl;
    private HttpClient client;
    
    public Requester(String baseUrl) {
        this.baseUrl = URI.create(baseUrl);
        this.client = HttpClient.newHttpClient();
    }

    public String get(String path, Map<String, String> headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(this.baseUrl.resolve(path)).GET();
        HttpResponse<String> resp = this.client.send(this.applyHeaders(builder, headers).build(), HttpResponse.BodyHandlers.ofString());
        
        return resp.body().toString();
    }

    public String post(String path, Map<String, String> headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(this.baseUrl.resolve(path)).POST(HttpRequest.BodyPublishers.ofString(toForm(headers)));
        HttpRequest request = this.applyHeaders(builder, headers).build();
        HttpResponse<String> resp = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        return resp.body().toString();
    }

    private HttpRequest.Builder applyHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            builder = builder.header(key, headers.get(key));
        }
        return builder;
    }

    private String toForm(Map<String, String> data) {
        return "";
    }

}