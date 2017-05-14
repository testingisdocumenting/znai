package com.twosigma.documentation.codesnippets;

import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.utils.JsonUtils;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class JsBasedCodeSnippetsTokenizer implements CodeTokenizer {
    private NashornEngine nashornEngine;

    public JsBasedCodeSnippetsTokenizer(NashornEngine nashornEngine) {
        this.nashornEngine = nashornEngine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> tokenize(String lang, String snippet) {
        nashornEngine.bind("snippet", snippet);
        nashornEngine.bind("lang", lang);
        String json = (String) nashornEngine.eval("JSON.stringify(parseCode(lang, snippet))");

        return (List<Map<String, Object>>) JsonUtils.deserializeAsList(json);
    }
}
