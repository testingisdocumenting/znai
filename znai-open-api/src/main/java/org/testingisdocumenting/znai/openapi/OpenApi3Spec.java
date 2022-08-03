/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.openapi;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.testingisdocumenting.znai.utils.StringUtils.nullAsEmpty;

public class OpenApi3Spec {
    private final String spec;
    private final SwaggerParseResult parseResult;
    private final List<OpenApi3Operation> operations;

    private OpenApi3Spec(String spec) {
        this.spec = spec;
        this.operations = new ArrayList<>();

        OpenAPIParser parser = new OpenAPIParser();

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);

        parseResult = parser.readContents(spec, null, parseOptions);
        process();
    }

    public static OpenApi3Spec parse(String spec) {
        return new OpenApi3Spec(spec);
    }

    public List<OpenApi3Operation> getOperations() {
        return operations;
    }

    public OpenApi3Operation findById(String id) {
        return operations.stream()
                .filter(op -> id.equals(op.getId()))
                .findFirst().orElse(null);
    }

    private void process() {
        Paths paths = parseResult.getOpenAPI().getPaths();
        for (Map.Entry<String, PathItem> pathEntry : paths.entrySet()) {
            String path = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();

            handleOperation(path, "GET", pathItem.getGet());
            handleOperation(path, "POST", pathItem.getPost());
            handleOperation(path, "PUT", pathItem.getPut());
            handleOperation(path, "DELETE", pathItem.getDelete());
            handleOperation(path, "PATCH", pathItem.getPatch());
        }
    }

    private void handleOperation(String path, String method, Operation parsed) {
        if (parsed == null) {
            return;
        }

        OpenApi3Operation operation = new OpenApi3Operation();
        operation.setId(Objects.toString(parsed.getOperationId()));
        operation.setTags(parsed.getTags());
        operation.setPath(path);
        operation.setMethod(method);
        operation.setSummary(parsed.getSummary());
        operation.setDescription(parsed.getDescription());

        List<Parameter> parsedParameters = parsed.getParameters();
        if (parsedParameters != null) {
            parsedParameters.forEach(parsedParameter -> operation.addParameter(convertParameter(parsedParameter)));
        }

        RequestBody requestBody = parsed.getRequestBody();
        if (requestBody != null) {
            OpenApi3Request requests = new OpenApi3Request(requestBody.getDescription(), convertContent(requestBody.getContent()));
            operation.setRequest(requests);
        }

        ApiResponses parsedResponses = parsed.getResponses();

        parsedResponses.forEach((code, parsedResponse) -> {
            OpenApi3Response response = new OpenApi3Response(
                    code,
                    parsedResponse.getDescription(),
                    convertContent(parsedResponse.getContent()));
            operation.addResponse(response);
        });

        operations.add(operation);
    }

    private static OpenApi3Parameter convertParameter(Parameter parsedParameter) {
        OpenApi3Parameter parameter = new OpenApi3Parameter();
        parameter.setName(parsedParameter.getName());
        parameter.setDescription(nullAsEmpty(parsedParameter.getDescription()));
        parameter.setIn(nullAsEmpty(parsedParameter.getIn()));
        parameter.setRequired(nullAsFalse(parsedParameter.getRequired()));
        parameter.setDeprecated(nullAsFalse(parsedParameter.getDeprecated()));
        parameter.setSchema(convertSchema(parsedParameter.getSchema()));

        return parameter;
    }

    private static OpenApi3Content convertContent(Content parsedContent) {
        if (parsedContent == null) {
            return new OpenApi3Content();
        }

        OpenApi3Content result = new OpenApi3Content();
        parsedContent.forEach((mimeType, media) -> result.register(mimeType, convertSchema(media.getSchema())));

        return result;
    }

    private static OpenApi3Schema convertSchema(Schema<?> schema) {
        return OpenApi3Schema.convertFromParsed("", schema);
    }

    private static boolean nullAsFalse(Boolean value) {
        return value != null && value;
    }
}
