package com.twosigma.documentation.codesnippets;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class CodeToken {
    private String type;
    private String data;

    public CodeToken(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("data", data);

        return result;
    }
}
