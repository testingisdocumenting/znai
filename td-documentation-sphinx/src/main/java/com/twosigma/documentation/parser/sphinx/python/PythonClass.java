package com.twosigma.documentation.parser.sphinx.python;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class PythonClass {
    private String refId;
    private String name;
    private String description;
    private List<PythonFunction> methods;

    public PythonClass(String refId, String name, String description) {
        this.refId = refId;
        this.name = name;
        this.description = description;
        methods = new ArrayList<>();
    }

    public void addMethod(PythonFunction method) {
        methods.add(method);
    }

    public String getRefId() {
        return refId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PythonFunction> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "PythonClass{" +
                "refId='" + refId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", methods=" + methods +
                '}';
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("refId", refId);
        result.put("name", name);
        result.put("description", description);
        result.put("methods", methods.stream().map(PythonFunction::toMap).collect(toList()));

        return result;
    }
}
