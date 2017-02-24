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
    public List<CodeToken> tokenize(String lang, String snippet) {
        nashornEngine.bind("snippet", snippet);
        nashornEngine.bind("lang", lang);
        String json = (String) nashornEngine.eval("JSON.stringify(parseCode(lang, snippet))");

        List<Map<String, String>> tokens = (List<Map<String, String>>) JsonUtils.deserializeAsList(json);
        return tokens.stream().map(this::createCodeToken).collect(toList());
    }

    private CodeToken createCodeToken(Map<String, String> token) {
        return new CodeToken(token.get("type"), token.get("data"));
    }
}
