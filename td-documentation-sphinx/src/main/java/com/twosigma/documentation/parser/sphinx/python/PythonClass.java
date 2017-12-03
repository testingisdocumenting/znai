package com.twosigma.documentation.parser.sphinx.python;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class PythonClass {
    private String name;
    private String description;
    private List<PythonMethod> methods;

    public PythonClass(String name, String description) {
        this.name = name;
        this.description = description;
        methods = new ArrayList<>();
    }

    public void addMethod(PythonMethod method) {
        methods.add(method);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PythonMethod> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "PythonClass{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", methods=" + methods +
                '}';
    }
}
