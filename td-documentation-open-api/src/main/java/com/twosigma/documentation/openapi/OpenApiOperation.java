package com.twosigma.documentation.openapi;

import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class OpenApiOperation {
    private String id;
    private String method;
    private String path;
    private List<String> tags;
    private List<DocElement> description;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<DocElement> getDescription() {
        return description;
    }

    public void setDescription(List<DocElement> description) {
        this.description = description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("method", method);
        result.put("path", path);
        result.put("tags", tags);
        result.put("parameters", parameters);
        result.put("responses", responses);
        result.put("description", description.stream().map(DocElement::toMap).collect(toList()));

        return result;
    }

}
