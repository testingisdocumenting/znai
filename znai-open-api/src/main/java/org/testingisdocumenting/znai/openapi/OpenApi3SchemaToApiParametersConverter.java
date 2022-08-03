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
import org.testingisdocumenting.znai.extensions.api.ApiParameter;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

public class OpenApi3SchemaToApiParametersConverter {
    private final OpenApiMarkdownParser parser;
    private final OpenApi3Schema rootSchema;
    private final ApiParameters apiParameters;

    public OpenApi3SchemaToApiParametersConverter(OpenApiMarkdownParser parser, String anchorPrefix, OpenApi3Schema rootSchema) {
        this.parser = parser;
        this.rootSchema = rootSchema;
        this.apiParameters = new ApiParameters(anchorPrefix);
    }

    public ApiParameters convert() {
        handleSchema(apiParameters.getRoot(), rootSchema);
        return apiParameters;
    }

    private void handleSchema(ApiParameter parent, OpenApi3Schema schema) {
        String type = schema.getType();
        if (type.equals("object")) {
            handleObjectSchema(parent, schema);
        } else if (type.equals("array")) {
            handleArraySchema(parent, schema);
        } else {
            handleGenericSchema(parent, schema);
        }
    }

    private void handleObjectSchema(ApiParameter parent, OpenApi3Schema schema) {
        ApiParameter newParent = schema.getName().isEmpty() ? parent : addParameter(parent, schema);

        for (OpenApi3Schema property : schema.getProperties()) {
            handleSchema(newParent, property);
        }
    }

    private void handleArraySchema(ApiParameter parent, OpenApi3Schema schema) {
        ApiParameter newParent = addParameter(parent, schema);
        OpenApi3Schema items = schema.getItems();
        if (items.getType().equals("object")) {
            handleSchema(newParent, items);
        }
    }

    private void handleGenericSchema(ApiParameter parent, OpenApi3Schema schema) {
        addParameter(parent, schema);
    }

    private ApiParameter addParameter(ApiParameter parent, OpenApi3Schema schema) {
        DocElement docElementFromDescription = parser.docElementFromDescription(schema.getDescription());

        return parent.add(
                schema.getName(),
                new ApiLinkedText(schema.renderCombinedType()),
                docElementFromDescription.contentToListOfMaps(), schema.getDescription());
    }
}
