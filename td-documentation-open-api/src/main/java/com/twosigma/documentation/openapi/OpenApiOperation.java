package com.twosigma.documentation.openapi;

import com.twosigma.documentation.parser.docelement.DocElement;

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
    private List<String> tags = new ArrayList<>();
    private List<DocElement> description = new ArrayList<>();

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

    public boolean hasTags(List<String> tagsToCheck) {
        return tagsToCheck.stream().allMatch(t -> this.tags.contains(t));
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
        result.put("summary", summary);
        result.put("tags", tags);
        result.put("parameters", parameters.stream().map(OpenApiParameter::toMap).collect(toList()));
        result.put("responses", responses);
        result.put("description", description.stream().map(DocElement::toMap).collect(toList()));

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
