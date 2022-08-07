/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.openapi;

import java.util.ArrayList;
import java.util.List;

public class OpenApi3Operation {
    private String id;
    private String method;
    private String summary;
    private String path;
    private List<String> tags;
    private String description;

    private OpenApi3Request request;
    private final List<OpenApi3Response> responses;
    private final List<OpenApi3Parameter> parameters;

    public OpenApi3Operation() {
        tags = new ArrayList<>();
        responses = new ArrayList<>();
        parameters = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method.toLowerCase();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public OpenApi3Request getRequest() {
        return request;
    }

    public void setRequest(OpenApi3Request request) {
        this.request = request;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addParameter(OpenApi3Parameter parameter) {
        parameters.add(parameter);
    }

    public List<OpenApi3Parameter> getParameters() {
        return parameters;
    }

    public void addResponse(OpenApi3Response response) {
        responses.add(response);
    }

    public boolean matches(String method, String path) {
        return this.method.equals(method) && this.path.equals(path);
    }

    public List<OpenApi3Response> getResponses() {
        return responses;
    }
}
