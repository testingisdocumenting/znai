package com.twosigma.documentation.extensions.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ApiParameter {
    private final String name;
    private final String type;
    private final List<Map<String, Object>> description;

    private final List<ApiParameter> children;

    public ApiParameter(String name, String type, List<Map<String, Object>> description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<Map<String, Object>> getDescription() {
        return description;
    }

    public List<ApiParameter> getChildren() {
        return children;
    }

    public void add(String name, String type, List<Map<String, Object>> description) {
        ApiParameter apiParameter = new ApiParameter(name, type, description);
        children.add(apiParameter);
    }

    ApiParameter find(String name) {
        return children.stream()
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no children found with name: '" + name +
                        "' in '" + this.name + "'"));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("type", type);
        result.put("description", description);
        if (! children.isEmpty()) {
            result.put("children", children.stream().map(ApiParameter::toMap).collect(toList()));
        }

        return result;
    }
}
