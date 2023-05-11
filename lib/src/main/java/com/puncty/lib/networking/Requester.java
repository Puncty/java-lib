package com.puncty.lib.networking;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.puncty.lib.exceptions.BrokenResponse;

public class Requester {
    private String baseUrl;

    public Requester(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public RequesterResponse get(String path, Map<String, String> headers) throws IOException, InterruptedException, MalformedURLException {
        HttpURLConnection conn = createConnection(path, "GET");
        applyHeaders(conn, headers);
        conn.connect();
        return readRequest(conn);
    }

    public RequesterResponse get(String path) throws IOException, InterruptedException {
        return get(path, new HashMap<String, String>());
    }

    public RequesterResponse post(String path, Map<String, String> data, Map<String, String> headers) throws IOException, InterruptedException, BrokenResponse {
        return nonGetRequest(path, data, headers, "POST");
    }
    
    public RequesterResponse put(String path, Map<String, String> data, Map<String, String> headers) throws IOException, InterruptedException, BrokenResponse {
        return nonGetRequest(path, data, headers, "PUT");
    }

    public RequesterResponse delete(String path, Map<String, String> data, Map<String, String> headers) throws IOException, InterruptedException, BrokenResponse {
        return nonGetRequest(path, data, headers, "DELETE");
    }

    private RequesterResponse nonGetRequest(String path, Map<String, String> data, Map<String, String> headers, String method) throws IOException, InterruptedException, BrokenResponse {
        String url = path + "?" + toForm(data);
        HttpURLConnection conn = createConnection(url, method);
        applyHeaders(conn, headers);

        // // write data to be sent
        // conn.setDoOutput(true);
        // DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        // out.writeBytes(toForm(data));
        // out.flush();
        // out.close();

        conn.connect();
        return readRequest(conn);
    }

    private static void applyHeaders(HttpURLConnection conn, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            conn.setRequestProperty(key, headers.get(key));
        }
    }

    private HttpURLConnection createConnection(String path, String method) throws MalformedURLException, IOException {
        URL url = new URL(this.baseUrl + path);
        System.out.println(url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        return conn;
    }

    private RequesterResponse readRequest(HttpURLConnection conn) throws IOException {
        String content = "";
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            content = readStream(in);
        } catch (Exception e) {
            // do nothing
        } finally {
            conn.disconnect();
        }

        return new RequesterResponse(conn.getResponseCode(), content);
    }

    private String readStream(InputStream in) {
        try {
            return new String(in.readAllBytes(), "utf8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String toForm(Map<String, String> data) {
        StringBuffer str = new StringBuffer();

        for (String key : data.keySet()) {
            if (str.length() != 0) {
                str.append("&") ;
            }
            str.append(key + "=" + data.get(key));
        }

        return str.toString();
    }

}
