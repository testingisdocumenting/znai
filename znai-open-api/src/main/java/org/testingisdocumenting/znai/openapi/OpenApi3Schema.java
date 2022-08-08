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

import io.swagger.v3.oas.models.media.*;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenApi3Schema {
    private static final String ADDITIONAL_PROPS_NAME = "< * >";

    private final String name;
    private final String type;
    private final String description;
    private final List<OpenApi3Schema> properties;
    private final List<String> required;

    private String format;
    private Object example;

    private String defaultRendered;
    private List<String> enumValues;

    private OpenApi3Schema items;
    private OpenApi3Schema additionalProperties;

    public static OpenApi3Schema convertFromParsed(String name, Schema<?> parsed) {
        if (parsed instanceof ComposedSchema) {
            return convertFromComposed(name, (ComposedSchema) parsed);
        } else if (parsed instanceof ObjectSchema) {
            return convertFromObject(name, (ObjectSchema) parsed);
        } else if (parsed instanceof MapSchema) {
            return convertFromMap(name, (MapSchema) parsed);
        } else if (parsed instanceof ArraySchema) {
            return convertFromArray(name, (ArraySchema) parsed);
        } else if (parsed instanceof StringSchema) {
            return convertFromString(name, (StringSchema) parsed);
        }

        return defaultSchema(name, parsed);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public boolean isRequired(String name) {
        return required.contains(name);
    }

    public String renderTypeWithFormatOrEnum() {
        String typeClassifier = renderTypeClassifier();

        return !typeClassifier.isEmpty() ?
                type + "(" + typeClassifier + ")":
                type;
    }

    public String renderCombinedType() {
        if (getType().equals("array")) {
            return "array of " + getItems().renderTypeWithFormatOrEnum();
        }

        return renderTypeWithFormatOrEnum();
    }

    public String getDescription() {
        return description;
    }

    public String renderDescriptionWithExamplesAndEnums() {
        List<String> parts = new ArrayList<>();

        if (!description.isEmpty()) {
            parts.add(description);
        }

        if (example != null) {
            parts.add(renderExample());
        }

        if (enumValues != null) {
            parts.add(renderEnumValues());
        }

        return String.join(
                "\n\\\n", // hard line break
                parts);
    }

    public String renderTypeClassifier() {
        if (format != null) {
            return format;
        }

        if (enumValues != null) {
            return "enum";
        }

        return "";
    }

    private String renderEnumValues() {
        if (enumValues == null) {
            return "";
        }

        return "*Available Values*: " + enumValues.stream().map(v -> "`" + v + "`").collect(Collectors.joining(", "));
    }

    public String renderExample() {
        return example != null ?
                "*Example*: `" + example + "`" :
                "";
    }

    public OpenApi3Schema getItems() {
        return items;
    }

    void setItems(OpenApi3Schema items) {
        this.items = items;
    }

    public OpenApi3Schema getAdditionalProperties() {
        return additionalProperties;
    }

    public List<OpenApi3Schema> getProperties() {
        return properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void addRequired(List<String> names) {
        required.addAll(names);
    }

    OpenApi3Schema(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.properties = new ArrayList<>();
        this.required = new ArrayList<>();
    }

    private static OpenApi3Schema convertFromComposed(String name, ComposedSchema parsed) {
        String composedType = composedType(parsed);
        OpenApi3Schema schema = createSchema(name, composedType, parsed);
        if (parsed.getOneOf() != null) {
            populateComposed(schema, composedType, parsed.getOneOf());
        } else if (parsed.getAnyOf() != null) {
            populateComposed(schema, composedType, parsed.getAnyOf());
        }

        return schema;
    }

    private static String composedType(ComposedSchema schema) {
        if (schema.getOneOf() != null) {
            return "oneOf";
        } else if (schema.getAnyOf() != null) {
            return "anyOf";
        }

        return "unknown-composed-type";
    }

    private static void populateComposed(OpenApi3Schema parent, String composedType, List<Schema> list) {
        list.forEach((child) -> parent.properties.add(convertFromParsed("", child)));
    }

    private static OpenApi3Schema convertFromObject(String name, Schema<Object> parsed) {
        OpenApi3Schema schema = createSchema(name, parsed);

        Map<String, Schema> parsedProperties = parsed.getProperties();
        if (parsedProperties != null) {
            parsedProperties.forEach((propertyName, parsedSchema) -> schema.properties.add(convertFromParsed(propertyName, parsedSchema)));
        }

        if (parsed.getRequired() != null) {
            schema.required.addAll(parsed.getRequired());
        }

        return schema;
    }

    private static OpenApi3Schema convertFromMap(String name, MapSchema parsed) {
        OpenApi3Schema schema = convertFromObject(name, parsed);
        if (parsed.getAdditionalProperties() == null) {
            return schema;
        }

        if (parsed.getAdditionalProperties() instanceof Boolean && parsed.getAdditionalProperties().equals(true)) {
            schema.additionalProperties = createAnyTypeSchema();
        } else if (parsed.getAdditionalProperties() instanceof Schema) {
            schema.additionalProperties = convertFromParsed(ADDITIONAL_PROPS_NAME, (Schema<?>) parsed.getAdditionalProperties());
        }

        return schema;
    }

    private static OpenApi3Schema createAnyTypeSchema() {
        return new OpenApi3Schema(ADDITIONAL_PROPS_NAME, "any", "");
    }

    private static OpenApi3Schema convertFromArray(String name, ArraySchema parsed) {
        OpenApi3Schema result = createSchema(name, parsed);
        result.setItems(convertFromParsed("", parsed.getItems()));

        return result;
    }

    private static OpenApi3Schema convertFromString(String name, StringSchema parsed) {
        OpenApi3Schema result = createSchema(name, parsed);
        if (parsed.getEnum() != null) {
            result.enumValues = new ArrayList<>(parsed.getEnum());
        }

        return result;
    }

    private static OpenApi3Schema defaultSchema(String name, Schema<?> parsed) {
        return createSchema(name, parsed);
    }

    private static OpenApi3Schema createSchema(String name, Schema<?> parsed) {
        return createSchema(name, parsed.getType(), parsed);
    }

    private static OpenApi3Schema createSchema(String name, String type, Schema<?> parsed) {
        OpenApi3Schema schema = new OpenApi3Schema(name,
                type,
                StringUtils.nullAsEmpty(parsed.getDescription()));

        if (parsed.getFormat() != null) {
            schema.setFormat(parsed.getFormat());
        }

        if (parsed.getExampleSetFlag()) {
            schema.setExample(parsed.getExample());
        }

        return schema;
    }
}
