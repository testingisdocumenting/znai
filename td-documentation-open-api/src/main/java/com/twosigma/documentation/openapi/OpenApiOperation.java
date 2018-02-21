package com.twosigma.documentation.openapi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OpenApiOperation {
    private String id;
    private String method;
    private String path;
    private String tag;

    private List<Map<String, ?>> parameters;
    private List<Map<String, ?>> responses;

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

    public List<Map<String, ?>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Map<String, ?>> parameters) {
        this.parameters = parameters;
    }

    public List<Map<String, ?>> getResponses() {
        return responses;
    }

    public void setResponses(List<Map<String, ?>> responses) {
        this.responses = responses;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("method", method);
        result.put("path", path);
        result.put("tag", tag);
        result.put("parameters", parameters);
        result.put("responses", responses);

        return result;
    }

}
