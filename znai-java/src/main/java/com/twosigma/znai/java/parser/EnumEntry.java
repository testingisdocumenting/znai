package com.twosigma.znai.java.parser;

public class EnumEntry {
    private String name;
    private String javaDocText;
    private boolean isDeprecated;

    public EnumEntry(String name, String javaDocText, boolean isDeprecated) {
        this.name = name;
        this.javaDocText = javaDocText;
        this.isDeprecated = isDeprecated;
    }

    public String getName() {
        return name;
    }

    public String getJavaDocText() {
        return javaDocText;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    @Override
    public String toString() {
        return "EnumEntry{" +
                "name='" + name + '\'' +
                ", javaDocText='" + javaDocText + '\'' +
                ", isDeprecated=" + isDeprecated +
                '}';
    }
}
