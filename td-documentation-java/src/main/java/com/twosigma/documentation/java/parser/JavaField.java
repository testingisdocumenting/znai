package com.twosigma.documentation.java.parser;

public class JavaField {
    private String name;
    private String javaDocText;

    public JavaField(String name, String javaDocText) {
        this.name = name;
        this.javaDocText = javaDocText;
    }

    public String getName() {
        return name;
    }

    public String getJavaDocText() {
        return javaDocText;
    }

    @Override
    public String toString() {
        return "JavaField{" +
                "name='" + name + '\'' +
                ", javaDocText='" + javaDocText + '\'' +
                '}';
    }
}
