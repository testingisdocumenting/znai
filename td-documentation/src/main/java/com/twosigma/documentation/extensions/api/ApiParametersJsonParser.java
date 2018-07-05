package com.twosigma.documentation.extensions.api;

import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ApiParametersJsonParser {
    private final ApiParameters apiParameters;
    private final Deque<ApiParameter> currentStack;
    private final MarkupParser markupParser;
    private final List<Map<String, Object>> json;
    private final Path path;

    public static ApiParameters parse(MarkupParser markupParser, String jsonContent) {
        return new ApiParametersJsonParser(markupParser, jsonContent).parse();
    }

    @SuppressWarnings("unchecked")
    private ApiParametersJsonParser(MarkupParser markupParser, String jsonContent) {
        this.markupParser = markupParser;
        this.json = (List<Map<String, Object>>) JsonUtils.deserializeAsList(jsonContent);
        this.apiParameters = new ApiParameters();
        this.currentStack = new ArrayDeque<>(Collections.singleton(apiParameters.getRoot()));
        this.path = Paths.get("");
    }

    public ApiParameters parse() {
        json.forEach(this::parseParam);

        return apiParameters;
    }

    @SuppressWarnings("unchecked")
    private void parseParam(Map<String, Object> param) {
        ApiParameter current = currentStack.getLast();
        ApiParameter apiParameter = current.add(param.get("name").toString(),
                param.get("type").toString(),
                markupParser.parse(path, param.get("description").toString()).contentToListOfMaps());

        currentStack.add(apiParameter);

        Object children = param.get("children");
        if (children != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) children;
            list.forEach(this::parseParam);
        }

        currentStack.removeLast();
    }
}
