/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions.api;

import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.utils.JsonUtils;

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
        MarkupParserResult parserResult = markupParser.parse(path, param.get("description").toString());
        ApiParameter apiParameter = current.add(param.get("name").toString(),
                param.get("type").toString(),
                parserResult.contentToListOfMaps(),
                parserResult.getAllText());

        Object children = param.get("children");
        if (children != null) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) children;
            list.forEach(p -> parseParam(apiParameter, p));
        }
    }
}
