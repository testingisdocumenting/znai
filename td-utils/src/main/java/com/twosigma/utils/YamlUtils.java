package com.twosigma.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Map;

public class YamlUtils {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @SuppressWarnings("unchecked")
    public static Map<String, ?> deserializeAsMap(String yaml) {
        try {
            return mapper.readValue(yaml, Map.class);
        } catch (IOException e) {
            throw new YamlParseException(e.getMessage());
        }
    }
}
