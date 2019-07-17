package com.twosigma.znai.extensions.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ApiParameters {
    private ApiParameter root;

    ApiParameters() {
        root = new ApiParameter("root", "root", Collections.emptyList());
    }

    public void add(String name, String type, List<Map<String, Object>> description) {
        root.add(name, type, description);
    }

    public ApiParameter find(String name) {
        return root.find(name);
    }

    public ApiParameter getRoot() {
        return root;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("parameters", root.getChildren().stream().map(ApiParameter::toMap).collect(toList()));

        return result;
    }
}
