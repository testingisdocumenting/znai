package com.twosigma.znai.typescript;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TypeScriptJsxDeclaration {
    private String tagName;
    private List<TypeScriptJsxAttr> attributes;

    @SuppressWarnings("unchecked")
    public TypeScriptJsxDeclaration(Map<String, ?> entry) {
        this.tagName = entry.get("tagName").toString();

        List<Map<String, ?>> attrs = (List<Map<String, ?>>) entry.get("attributes");
        this.attributes = attrs.stream()
                .map(TypeScriptJsxAttr::new)
                .collect(Collectors.toList());
    }

    public String getTagName() {
        return tagName;
    }

    public List<TypeScriptJsxAttr> getAttributes() {
        return attributes;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tagName", tagName);
        result.put("attributes", attributes.stream().map(TypeScriptJsxAttr::toMap).collect(toList()));

        return result;
    }
}
