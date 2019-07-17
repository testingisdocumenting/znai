package com.twosigma.znai.extensions;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class HttpResource {
    private final String url;
    private String content;

    HttpResource(String url) {
        this.url = url;
        this.content = get();
    }

    public static boolean isHttpResource(String path) {
        return path.startsWith("http://") || path.startsWith("https://");
    }

    public String getContent() {
        return content;
    }

    public boolean exists() {
        return content != null;
    }

    private String get() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*");

            if (connection.getResponseCode() >= 400) {
                return null;
            }

            return extractHttpResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException("couldn't get " + url, e);
        }
    }

    private String extractHttpResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        return inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : "";
    }
}
