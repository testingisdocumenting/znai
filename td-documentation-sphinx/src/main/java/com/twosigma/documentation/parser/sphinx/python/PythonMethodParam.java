package com.twosigma.documentation.parser.sphinx.python;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class PythonMethodParam {
    private String name;
    private String description;

    public PythonMethodParam(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "PythonMethodParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("description", description);

        return result;
    }
}
