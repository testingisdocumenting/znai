package com.twosigma.testing.webtau.cfg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class ConfigValue {
    private String key;
    private String prefixedUpperCaseKey;
    private Supplier<Object> defaultValueSupplier;
    private String description;

    private Deque<Value> values;

    public static ConfigValue declare(String key, String description, Supplier<Object> defaultValueSupplier) {
        return new ConfigValue(key,  description,null, null, defaultValueSupplier);
    }

    public void set(String source, Object value) {
        values.addFirst(new Value(source, value));
    }

    public void accept(String source, Map configValues) {
        if (configValues.containsKey(key)) {
            set(source, configValues.get(key));
        } else if (configValues.containsKey(prefixedUpperCaseKey)) {
            set(source, configValues.get(prefixedUpperCaseKey));
        }
    }

    public String getKey() {
        return key;
    }

    public String getPrefixedUpperCaseKey() {
        return prefixedUpperCaseKey;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return (isDefault() ? "default" : values.getFirst().getSourceId());
    }

    public List<String> getSources() {
        return (isDefault() ?
                Collections.singletonList("default") :
                values.stream().map(Value::getSourceId).collect(Collectors.toList()));
    }

    public String getAsString() {
        return isDefault() ? defaultValueSupplier.get().toString() : values.getFirst().getValue().toString();
    }

    public Path getAsPath() {
        return isDefault() ? (Path) defaultValueSupplier.get() : Paths.get(values.getFirst().getValue().toString());
    }

    public int getAsInt() {
        if (isDefault()) {
           return (int) defaultValueSupplier.get();
        }

        Object first = values.getFirst().getValue();
        return first instanceof Integer ?
                (int) first :
                Integer.valueOf(first.toString());
    }

    public boolean getAsBoolean() {
        if (isDefault()) {
            return (boolean) defaultValueSupplier.get();
        }

        Object first = values.getFirst().getValue();
        return first.toString().toLowerCase().equals("true");
    }

    @Override
    public String toString() {
        return key + ": " + values.stream().map(Value::toString).collect(Collectors.joining(", "));
    }

    private ConfigValue(String key, String description, String sourceId, Object value, Supplier<Object> defaultValueSupplier) {
        this.key = key;
        this.prefixedUpperCaseKey = "WEBTAU_" + key.toUpperCase();
        this.description = description;
        this.values = new ArrayDeque<>();
        this.defaultValueSupplier = defaultValueSupplier;
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

        public String getSourceId() {
            return sourceId;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value + " (" + sourceId + ")";
        }
    }
}
