package com.twosigma.documentation.codesnippets;

import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.utils.JsonUtils;

import java.util.List;

/**
 * @author mykola
 */
public class JsBasedCodeSnippetsTokenizer implements CodeTokenizer {
    private NashornEngine nashornEngine;

    public JsBasedCodeSnippetsTokenizer(NashornEngine nashornEngine) {
        this.nashornEngine = nashornEngine;
    }

    @Override
    public List<?> tokenize(String lang, String snippet) {
        nashornEngine.bind("snippet", snippet);
        nashornEngine.bind("lang", lang);
        String json = (String) nashornEngine.eval("JSON.stringify(parseCode(lang, snippet))");

        return JsonUtils.deserializeAsList(json);
    }
}
