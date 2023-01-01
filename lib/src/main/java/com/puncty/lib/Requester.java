package com.puncty.lib;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;

public class Requester {
    private URI baseUrl;
    private HttpClient client;

    public static void main(String[] args) throws IOException, InterruptedException {
        var r = new Requester("http://localhost:3000");
        Map<String, String> m = new HashMap<String, String>();
        m.put("name", "Tch1b0");
        m.put("password", "test");
        m.put("email-address", "Tch1b0@gmx.com");
        System.out.println(r.post("/account/register", m));
    }

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

    public RequesterResponse post(String path, Map<String, String> headers) throws IOException, InterruptedException {
        URI resPath = this.baseUrl.resolve(path + "?" + toForm(headers));
        HttpRequest.Builder builder = HttpRequest.newBuilder(resPath).POST(BodyPublishers.ofString(toForm(headers)));
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
