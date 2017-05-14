package com.twosigma.documentation.codesnippets;

import com.twosigma.utils.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class CodeSnippetsProps {
    public static Map<String, Object> create(CodeTokenizer codeTokenizer, String lang, String snippet) {
        List<?> codeTokens = lang.isEmpty() ?
                Collections.singletonList(textToken(snippet)):
                codeTokenizer.tokenize(lang, snippet);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("lang", lang);
        result.put("maxLineLength", StringUtils.maxLineLength(snippet));
        result.put("tokens", codeTokens);

        return result;
    }

    private static Map<String, Object> textToken(String text) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "text");
        result.put("content", text);
        return result;
    }
}