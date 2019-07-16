package com.twosigma.documentation.typescript;

import java.util.Map;

public class TypeScriptProperty {
    private String name;
    private String type;
    private String documentation;

    public TypeScriptProperty(String name, String type, String documentation) {
        this.name = name;
        this.type = type;
        this.documentation = documentation;
    }

    public TypeScriptProperty(Map<String, ?> entry) {
        this(entry.get("name").toString(),
                entry.get("type").toString(),
                entry.get("documentation").toString());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDocumentation() {
        return documentation;
    }

    @Override
    public String toString() {
        return "TypeScriptProperty{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", documentation='" + documentation + '\'' +
                '}';
    }
}
