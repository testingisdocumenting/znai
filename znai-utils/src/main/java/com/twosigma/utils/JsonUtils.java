/*
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

package com.twosigma.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private static ObjectMapper createDeserializeMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

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
