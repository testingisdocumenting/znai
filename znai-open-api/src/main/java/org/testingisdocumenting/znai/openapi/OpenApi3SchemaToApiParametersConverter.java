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

    private boolean createNewParent;

    public OpenApi3SchemaToApiParametersConverter(OpenApiMarkdownParser parser, String anchorPrefix, OpenApi3Schema rootSchema) {
        this.parser = parser;
        this.rootSchema = rootSchema;
        this.apiParameters = new ApiParameters(anchorPrefix);
        this.createNewParent = true;
    }

    public ApiParameters convert() {
        handleSchema(apiParameters.getRoot(), rootSchema);

        if (rootSchema.getType().equals("object")) {
            return apiParameters.withoutTopLevel();
        }

        return apiParameters;
    }

    private void handleSchema(ApiParameter parent, OpenApi3Schema schema) {
        String type = schema.getType();
        switch (type) {
            case "object":
                handleObjectSchema(parent, schema);
                break;
            case "anyOf":
            case "oneOf":
                handleComposeSchema(parent, schema);
                break;
            case "array":
                handleArraySchema(parent, schema);
                break;
            default:
                handleGenericSchema(parent, schema);
                break;
        }
    }

    private void handleComposeSchema(ApiParameter parent, OpenApi3Schema schema) {
        ApiParameter newParent = addParameter(parent, schema);

        for (OpenApi3Schema property : schema.getProperties()) {
            handleSchema(newParent, property);
        }
    }

    private void handleObjectSchema(ApiParameter parent, OpenApi3Schema schema) {
        ApiParameter newParent = !createNewParent ? parent : addParameter(parent, schema);

        for (OpenApi3Schema property : schema.getProperties()) {
            handleSchema(newParent, property);
        }
    }

    private void handleArraySchema(ApiParameter parent, OpenApi3Schema schema) {
        ApiParameter newParent = addParameter(parent, schema);
        OpenApi3Schema items = schema.getItems();
        if (items.getType().equals("object")) {
            createNewParent = false;
            handleSchema(newParent, items);
            createNewParent = true;
        }
    }

    private void handleGenericSchema(ApiParameter parent, OpenApi3Schema schema) {
        addParameter(parent, schema);
    }

    private ApiParameter addParameter(ApiParameter parent, OpenApi3Schema schema) {
        DocElement docElementFromDescription = parser.docElementFromDescription(schema.renderDescriptionWithExamples());

        return parent.add(
                schema.getName(),
                new ApiLinkedText(schema.renderCombinedType()),
                docElementFromDescription.contentToListOfMaps(), schema.getDescription());
    }
}
