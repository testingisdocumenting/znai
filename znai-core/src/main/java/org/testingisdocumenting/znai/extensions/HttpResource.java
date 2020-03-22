/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions;

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
        } catch (Exception e) {
            return null;
        }
    }

    private String extractHttpResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        return inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : "";
    }
}
