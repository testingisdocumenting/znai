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

package org.testingisdocumenting.znai.openapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class OpenApiOperation {
    private String id;
    private String method;
    private String summary;
    private String path;
    private List<String> consumes;
    private List<String> produces;
    private List<String> tags = new ArrayList<>();
    private List<Map<String, Object>> description = new ArrayList<>();

    private List<OpenApiParameter> parameters = new ArrayList<>();
    private List<Map<String, ?>> responses = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must be not null");
        }

        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public List<OpenApiParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<OpenApiParameter> parameters) {
        this.parameters = parameters;
    }

    public List<Map<String, ?>> getResponses() {
        return responses;
    }

    public void setResponses(List<Map<String, ?>> responses) {
        this.responses = responses;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean matches(String method, String path) {
        return this.method.equals(method) && this.path.equals(path);
    }

    public boolean hasTags(List<String> tagsToCheck) {
        return tags != null && this.tags.containsAll(tagsToCheck);
    }

    public List<Map<String, Object>> getDescription() {
        return description;
    }

    public void setDescription(List<Map<String, Object>> description) {
        this.description = description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("method", method);
        result.put("path", path);
        result.put("consumes", consumes);
        result.put("produces", produces);
        result.put("summary", summary);
        result.put("tags", tags);
        result.put("parameters", parameters.stream().map(OpenApiParameter::toMap).collect(toList()));
        result.put("responses", responses);
        result.put("description", description);

        return result;
    }

    @Override
    public String toString() {
        return "OpenApiOperation{" +
                "id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", summary='" + summary + '\'' +
                ", path='" + path + '\'' +
                ", tags=" + tags +
                ", description=" + description +
                ", parameters=" + parameters +
                ", responses=" + responses +
                '}';
    }
}
