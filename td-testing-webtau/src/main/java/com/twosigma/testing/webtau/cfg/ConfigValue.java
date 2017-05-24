package com.twosigma.testing.webtau.cfg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class ConfigValue {
    private String key;
    private Object defaultValue;
    private String description;

    private Deque<Value> values;

    public static ConfigValue declare(String key, String description, Object defaultValue) {
        return new ConfigValue(key,  description,null, null, defaultValue);
    }

    public void set(String source, Object value) {
        values.addFirst(new Value(source, value));
    }

    public void accept(String source, Map values) {
        if (values.containsKey(key)) {
            set(source, values.get(key));
        }
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return (isDefault() ? "default" : values.getFirst().sourceId);
    }

    public String getAsString() {
        return isDefault() ? defaultValue.toString() : values.getFirst().value.toString();
    }

    public Path getAsPath() {
        return isDefault() ? (Path) defaultValue : Paths.get(values.getFirst().value.toString());
    }

    public int getAsInt() {
        if (isDefault()) {
           return (int) defaultValue;
        }

        Object first = values.getFirst().value;
        return first instanceof Integer ?
                (int) first :
                Integer.valueOf(first.toString());
    }

    @Override
    public String toString() {
        return key + ": " + values.stream().map(Value::toString).collect(Collectors.joining(", "));
    }

    private ConfigValue(String key, String description, String sourceId, Object value, Object defaultValue) {
        this.key = key;
        this.description = description;
        this.values = new ArrayDeque<>();
        this.defaultValue = defaultValue;
    }

    public boolean isDefault() {
        return values.isEmpty();
    }

    public boolean nonDefault() {
        return ! isDefault();
    }

    private static class Value {
        private String sourceId;
        private Object value;

        public Value(String sourceId, Object value) {
            this.sourceId = sourceId;
            this.value = value;
        }

        @Override
        public String toString() {
            return value + " (" + sourceId + ")";
        }
    }
}
