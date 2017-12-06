package com.twosigma.documentation.parser.sphinx.python;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class PythonMethod {
    private String name;
    private String description;
    private List<PythonMethodParam> params;

    public PythonMethod(String name, String description) {
        this.name = name;
        this.description = description;
        this.params = new ArrayList<>();
    }

    public void addParam(PythonMethodParam param) {
        params.add(param);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PythonMethodParam> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "PythonMethod{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", params=" + params +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("params", params.stream().map(PythonMethodParam::toMap).collect(toList()));

        return result;
    }
}
