package com.twosigma.documentation.typescript;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeScriptType {
    private String name;
    private List<TypeScriptProperty> properties;

    @SuppressWarnings("unchecked")
    public TypeScriptType(Map<String, ?> entry) {
        name = entry.get("name").toString();

        List<Map<String, ?>> members = (List<Map<String, ?>>) entry.get("members");
        properties = members.stream()
                .filter(m -> "property".equals(m.get("kind")))
                .map(TypeScriptProperty::new)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<TypeScriptProperty> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "TypeScriptType{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
