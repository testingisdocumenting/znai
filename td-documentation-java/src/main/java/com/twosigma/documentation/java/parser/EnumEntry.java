package com.twosigma.documentation.java.parser;

/**
 * @author mykola
 */
public class EnumEntry {
    private String name;
    private String javaDocText;

    public EnumEntry(String name, String javaDocText) {
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
        return "EnumEntry{" +
                "name='" + name + '\'' +
                ", javaDocText='" + javaDocText + '\'' +
                '}';
    }
}
