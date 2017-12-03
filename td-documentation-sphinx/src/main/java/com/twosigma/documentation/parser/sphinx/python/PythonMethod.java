package com.twosigma.documentation.parser.sphinx.python;

import java.util.ArrayList;
import java.util.List;

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
}
