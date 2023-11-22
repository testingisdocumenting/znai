/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper serializeMapper = createDeserializeMapper();
    private static final ObjectMapper serializePrettyPrintMapper = createSerializePrettyPrintMapper();
    private static final ObjectMapper deserializeMapper = createDeserializeMapper();

    private JsonUtils() {
    }

    public static String serialize(Object json) {
        if (json == null) {
            return null;
        }

        try {
            return serializeMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public static String serializePrettyPrint(Object json) {
        if (json == null) {
            return null;
        }

        try {
            return serializePrettyPrintMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> deserializeAsMap(String json) {
        if (json == null) {
            return null;
        }

        try {
            return deserializeMapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static List<?> deserializeAsList(String json) {
        if (json == null) {
            return null;
        }

        try {
            return deserializeMapper.readValue(json, List.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static Object deserialize(String json) {
        if (json == null) {
            return null;
        }

        try {
            return deserializeMapper.readValue(json, Object.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    /**
     * check if a potential json string has matching {}
     * @param json potential json string
     * @return true if scope is fully closed
     */
    public static boolean isObjectScopeClosed(String json) {
        char previousChar = ' ';
        int scopeBalance = 0;
        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;
        boolean metScope = false;
        for (int idx = 0; idx < json.length(); idx++) {
            char c = json.charAt(idx);

            boolean isPreviousCharEscape = previousChar == '\\';
            if (!isPreviousCharEscape) {
                if (c == '\'') {
                    insideSingleQuote = !insideSingleQuote;
                }
                if (c == '"') {
                    insideDoubleQuote = !insideDoubleQuote;
                }
            }

            if (!insideDoubleQuote && !insideSingleQuote) {
                if (c == '{') {
                    metScope = true;
                    scopeBalance++;
                } else if (c == '}') {
                    scopeBalance--;
                }
            }

            previousChar = c;
        }

        return metScope && scopeBalance == 0;
    }

    private static ObjectMapper createDeserializeMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        mapper.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature(), true);

        return mapper;
    }

    private static ObjectMapper createSerializeMapper() {
        return new ObjectMapper();
    }

    private static ObjectMapper createSerializePrettyPrintMapper() {
        ObjectMapper mapper = createSerializeMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
