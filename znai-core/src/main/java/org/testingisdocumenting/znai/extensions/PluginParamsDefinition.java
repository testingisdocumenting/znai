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

package org.testingisdocumenting.znai.extensions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PluginParamsDefinition {
    static class Param {
        private final String name;
        private final String description;
        private final String example;
        private final PluginParamType type;
        private final boolean isRequired;

        Param(String name, PluginParamType type, String description, String example, boolean isRequired) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.example = example;
            this.isRequired = isRequired;
        }

        public String getName() {
            return name;
        }

        public boolean isRequired() {
            return isRequired;
        }

        @Override
        public String toString() {
            return name + ": " + (isRequired ? "REQUIRED " : "")
                    + description + " <" + type.description() + "> (e.g. " + example + ")";
        }
    }

    private final boolean isDefined;
    private final List<Param> params = new ArrayList<>();

    public static PluginParamsDefinition undefined() {
        return new PluginParamsDefinition(false);
    }

    public PluginParamsDefinition() {
        this(true);
    }

    PluginParamsDefinition(boolean isDefined) {
        this.isDefined = isDefined;
    }

    public boolean has(String name) {
        return params.stream().anyMatch(p -> p.name.equals(name));
    }

    public PluginParamsDefinition add(String name, PluginParamType type, String description, String example) {
        add(new Param(name, type, description, example, false));
        return this;
    }

    public PluginParamsDefinition add(String name, PluginParamType type, String description) {
        add(new Param(name, type, description, type.example(), false));
        return this;
    }

    public PluginParamsDefinition addRequired(String name, PluginParamType type, String description, String example) {
        add(new Param(name, type, description, example, true));
        return this;
    }

    public PluginParamsDefinition add(PluginParamsDefinition paramsDefinition) {
        paramsDefinition.params.forEach(this::add);
        return this;
    }

    private void add(Param param) {
        validateNameUniqueness(param.name);
        params.add(param);
    }

    private void validateNameUniqueness(String name) {
        boolean found = params.stream().anyMatch(p -> p.name.equals(name));
        if (found) {
            throw new IllegalArgumentException("parameter <" + name + "> is already registered");
        }
    }

    private Param findParam(String name) {
        return params.stream()
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public void validate(PluginParams pluginParams) {
        if (!isDefined) {
            return;
        }

        List<String> unrecognizedNames = new ArrayList<>();
        List<String> typeMismatches = new ArrayList<>();

        List<String> missingRequiredNames = params.stream()
                .filter(Param::isRequired)
                .filter(p -> !pluginParams.getOpts().has(p.name))
                .map(Param::getName)
                .collect(Collectors.toList());

        pluginParams.getOpts().forEach((name, value) -> {
            if (name.equals("meta")) {
                return; // ignore meta parameters for now
            }

            Param param = findParam(name);
            if (param == null) {
                unrecognizedNames.add(name);
            } else {
                boolean isTypeMatch = param.type.isValid(value);
                if (!isTypeMatch) {
                    typeMismatches.add(name + " given: " + renderGiven(value) +
                            ", expected: " + param.type.descriptionWithExample());
                }
            }
        });

        if (unrecognizedNames.isEmpty() && typeMismatches.isEmpty() && missingRequiredNames.isEmpty()) {
            return;
        }

        throw new IllegalArgumentException(renderValidationMessage(missingRequiredNames,
                unrecognizedNames, typeMismatches));
    }

    private String renderGiven(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\" <string>" ;
        }

        if (value instanceof Boolean) {
            return value + " <boolean>" ;
        }

        if (value instanceof Number) {
            return value + " <number>" ;
        }

        return value.toString();
    }

    private String renderValidationMessage(List<String> missingRequiredNames,
                                           List<String> unrecognizedNames,
                                           List<String> typeMismatches) {
        StringBuilder message = new StringBuilder();
        if (!missingRequiredNames.isEmpty()) {
            message.append("missing required parameter(s): ")
                    .append(String.join(", ", missingRequiredNames)).append("\n");
        }

        if (!unrecognizedNames.isEmpty()) {
            message.append("unrecognized parameter(s): ")
                    .append(String.join(", ", unrecognizedNames)).append("\n");
        }

        if (!typeMismatches.isEmpty()) {
            message.append("type mismatches:\n  ");
            message.append(String.join("\n  ", typeMismatches));
            message.append("\n");
        }

        message.append("\navailable plugin parameters:\n");

        params.stream()
                .sorted(Comparator.comparing(p -> p.name))
                .forEach(param -> message.append("  ").append(param).append("\n"));

        return message.toString();
    }

    @Override
    public String toString() {
        return params.stream()
                .map(Param::toString)
                .collect(Collectors.joining("\n"));
    }
}
