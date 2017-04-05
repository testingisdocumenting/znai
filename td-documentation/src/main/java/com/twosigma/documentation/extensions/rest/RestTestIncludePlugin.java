package com.twosigma.documentation.extensions.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.utils.JsonUtils;

/**
 * @author mykola
 */
public class RestTestIncludePlugin implements IncludePlugin {
    private int resultIdx;
    private Path markupPath;

    @Override
    public String id() {
        return "rest-test";
    }

    @Override
    public void reset(final IncludeContext context) {
        resultIdx = 0;
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, final IncludeParams includeParams) {
        this.markupPath = markupPath;

        Map testData = JsonUtils.deserializeAsMap(componentsRegistry.includeResourceResolver()
                .textContent(includeParams.getFreeParam()));
        String scenarioMarkup = testData.get("scenario").toString();

        List<DocElement> docElements = elementsFromScenario(componentsRegistry.parser(), scenarioMarkup);

        Map result = getResult(testData);
        docElements.add(urlAndMethod(result));
        docElements.add(new DocElement("Json", "data", result.get("body"),
                "paths", result.get("paths")));

        resultIdx++;
        return PluginResult.docElements(docElements.stream());
    }

    @Override
    public String textForSearch() {
        return "";
    }

    private Map getResult(Map testData) {
        List results = (List) testData.get("results");
        return (Map) results.get(resultIdx);
    }

    private DocElement urlAndMethod(Map result) {
        DocElement paragraph = new DocElement(DocElementType.PARAGRAPH);

        Object method = result.get("method");
        String preposition = method.equals("GET") ? "from" : "to";

        paragraph.addChild(new DocElement(DocElementType.INLINED_CODE, "code", method));
        paragraph.addChild(new DocElement(DocElementType.SIMPLE_TEXT, "text", " " + preposition + " "));
        paragraph.addChild(new DocElement(DocElementType.INLINED_CODE, "code", result.get("url")));

        return paragraph;
    }

    private List<DocElement> elementsFromScenario(MarkupParser parser, String scenario) {
        MarkupParserResult parserResult = parser.parse(markupPath, scenario);
        return parserResult.getDocElement().getContent();
    }
}
