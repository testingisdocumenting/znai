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

import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.List;

public class OpenApi3ParametersToApiParametersConverter {
    private final OpenApiMarkdownParser parser;
    private final List<OpenApi3Parameter> parameters;
    private final ApiParameters apiParameters;

    public OpenApi3ParametersToApiParametersConverter(OpenApiMarkdownParser parser, String anchorPrefix, List<OpenApi3Parameter> parameters) {
        this.parser = parser;
        this.parameters = parameters;
        this.apiParameters = new ApiParameters(anchorPrefix);
    }

    public ApiParameters convert() {
        parameters.forEach(this::convertParameter);

        return apiParameters;
    }

    private void convertParameter(OpenApi3Parameter parameter) {
        DocElement docElementFromDescription = parser.docElementFromDescription(parameter.getDescription());

        apiParameters.add(parameter.getName(),
                new ApiLinkedText(parameter.getSchema().renderCombinedType()),
                docElementFromDescription.contentToListOfMaps(), parameter.getDescription());
    }
}
