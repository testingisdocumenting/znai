package com.twosigma.znai.typescript;

import java.util.LinkedHashMap;
import java.util.Map;

public class TypeScriptJsxAttr {
    private String name;
    private String value;

    @SuppressWarnings("unchecked")
    public TypeScriptJsxAttr(Map<String, ?> entry) {
        this.name = entry.get("name").toString();
        this.value = entry.get("value").toString();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("value", value);

        return result;
    }
}
