package com.twosigma.documentation.parser.sphinx.python;

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
}
