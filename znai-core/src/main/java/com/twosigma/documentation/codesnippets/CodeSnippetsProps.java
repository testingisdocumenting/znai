package com.twosigma.documentation.codesnippets;

import java.util.LinkedHashMap;
import java.util.Map;

public class CodeSnippetsProps {
    public static Map<String, Object> create(String lang, String snippet) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("lang", lang);
        result.put("snippet", snippet);

        return result;
    }
}