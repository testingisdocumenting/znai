package com.twosigma.documentation.codesnippets;

import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.utils.JsonUtils;

import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class CodeSnippetsTokenizer {
    private NashornEngine nashornEngine;

    public CodeSnippetsTokenizer(NashornEngine nashornEngine) {
        this.nashornEngine = nashornEngine;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> tokenize(String snippet) {
        nashornEngine.bind("snippet", snippet);
        String json = (String) nashornEngine.eval("JSON.stringify(parseCode('cpp', snippet))");

        return (List<Map<String, String>>) JsonUtils.deserializeAsList(json);
    }
}
