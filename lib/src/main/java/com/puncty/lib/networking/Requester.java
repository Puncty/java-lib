package com.puncty.lib.networking;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Map;

public class Requester {
    private URI baseUrl;
    private HttpClient client;

    public Requester(String baseUrl) {
        this.baseUrl = URI.create(baseUrl);
        this.client = HttpClient.newHttpClient();
    }

    public RequesterResponse get(String path, Map<String, String> headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(this.baseUrl.resolve(path)).GET();
        HttpResponse<String> resp = this.client.send(applyHeaders(builder, headers).build(), HttpResponse.BodyHandlers.ofString());

        return new RequesterResponse(resp);
    }

    public RequesterResponse get(String path) throws IOException, InterruptedException {
        return get(path, Map.of());
    }

    public RequesterResponse post(String path, Map<String, String> data, Map<String, String> headers) throws IOException, InterruptedException {
        URI resPath = this.applyArgsToUri(baseUrl, data);
        HttpRequest.Builder builder = HttpRequest.newBuilder(resPath).POST(BodyPublishers.noBody());
        HttpRequest request = applyHeaders(builder, headers).build();
        HttpResponse<String> resp = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        return new RequesterResponse(resp);
    }
    
    public RequesterResponse put(String path, Map<String, String> data, Map<String, String> headers) throws IOException, InterruptedException {
        URI resPath = this.applyArgsToUri(baseUrl, data);
        HttpRequest.Builder builder = HttpRequest.newBuilder(resPath).PUT(BodyPublishers.noBody());
        HttpRequest request = applyHeaders(builder, headers).build();
        HttpResponse<String> resp = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        return new RequesterResponse(resp);
    }

    public RequesterResponse delete(String path, Map<String, String> data, Map<String, String> headers) throws IOException, InterruptedException {
        URI resPath = this.applyArgsToUri(baseUrl, data);
        HttpRequest.Builder builder = HttpRequest.newBuilder(resPath).DELETE();
        HttpRequest request = applyHeaders(builder, headers).build();
        HttpResponse<String> resp = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        return new RequesterResponse(resp);
    }

    public static HttpRequest.Builder applyHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            builder = builder.header(key, headers.get(key));
        }
        return builder;
    }

    private URI applyArgsToUri(URI path, Map<String, String> data) {
        return this.baseUrl.resolve(path + "?" + toForm(data));
    }

    private String toForm(Map<String, String> data) {
        String str = "";

        for (String key : data.keySet()) {
            if (!str.equals("")) {
                str += "&" ;
            }
            str += key + "=" + data.get(key);
        }

        return str;
    }

}
