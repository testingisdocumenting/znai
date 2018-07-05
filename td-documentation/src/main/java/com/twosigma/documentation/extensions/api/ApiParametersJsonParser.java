package com.twosigma.documentation.extensions.api;

import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ApiParametersJsonParser {
    private final ApiParameters apiParameters;
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
        this.path = Paths.get("");
    }

    public ApiParameters parse() {
        json.forEach(p -> parseParam(apiParameters.getRoot(), p));
        return apiParameters;
    }

    @SuppressWarnings("unchecked")
    private void parseParam(ApiParameter current, Map<String, Object> param) {
        ApiParameter apiParameter = current.add(param.get("name").toString(),
                param.get("type").toString(),
                markupParser.parse(path, param.get("description").toString()).contentToListOfMaps());

        Object children = param.get("children");
        if (children != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) children;
            list.forEach(p -> parseParam(apiParameter, p));
        }
    }
}
