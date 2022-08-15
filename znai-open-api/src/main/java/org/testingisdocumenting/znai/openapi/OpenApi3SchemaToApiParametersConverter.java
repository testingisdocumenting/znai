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
        handleSchema(apiParameters.getRoot(), rootSchema, false);

        if (rootSchema.getType() == null) {
            return apiParameters;
        }

        if (rootSchema.getType().equals("object")) {
            return apiParameters.withoutTopLevel();
        }

        return apiParameters;
    }

    private void handleSchema(ApiParameter parent, OpenApi3Schema schema, boolean required) {
        String type = schema.getType();
        if (type == null) {
            return;
        }

        switch (type) {
            case "object":
                handleObjectSchema(parent, schema, required);
                break;
            case "anyOf":
            case "oneOf":
                handleComposeSchema(parent, schema, required);
                break;
            case "array":
                handleArraySchema(parent, schema, required);
                break;
            default:
                handleGenericSchema(parent, schema, required);
                break;
        }
    }

    private void handleComposeSchema(ApiParameter parent, OpenApi3Schema schema, boolean required) {
        ApiParameter newParent = addParameter(parent, schema, required);

        for (OpenApi3Schema property : schema.getProperties()) {
            handleSchema(newParent, property, schema.isRequired(property.getName()));
        }
    }

    private void handleObjectSchema(ApiParameter parent, OpenApi3Schema schema, boolean required) {
        ApiParameter newParent = !createNewParent ? parent : addParameter(parent, schema, required);

        for (OpenApi3Schema property : schema.getProperties()) {
            handleSchema(newParent, property, schema.isRequired(property.getName()));
        }

        OpenApi3Schema additionalProperties = schema.getAdditionalProperties();
        if (additionalProperties != null) {
            handleSchema(newParent, schema.getAdditionalProperties(), false);
        }
    }

    private void handleArraySchema(ApiParameter parent, OpenApi3Schema schema, boolean required) {
        ApiParameter newParent = addParameter(parent, schema, required);
        OpenApi3Schema items = schema.getItems();
        if (items.getType().equals("object")) {
            createNewParent = false;
            handleSchema(newParent, items, false);
            createNewParent = true;
        }
    }

    private void handleGenericSchema(ApiParameter parent, OpenApi3Schema schema, boolean required) {
        addParameter(parent, schema, required);
    }

    private ApiParameter addParameter(ApiParameter parent, OpenApi3Schema schema, boolean required) {
        DocElement docElementFromDescription = parser.docElementFromDescription(schema.renderDescriptionWithExamplesAndEnums());

        String namePrefix = required ? "*" : "";
        return parent.add(
                schema.getName() + namePrefix,
                new ApiLinkedText(schema.renderCombinedType()),
                docElementFromDescription.contentToListOfMaps(), schema.getDescription());
    }
}
