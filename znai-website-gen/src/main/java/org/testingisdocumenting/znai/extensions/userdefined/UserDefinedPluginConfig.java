/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.userdefined;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.template.TextTemplate;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class UserDefinedPluginConfig {
    public enum PluginRole { INCLUDE, FENCE }

    private static final Map<String, PluginParamType> ARGUMENT_TYPES = Map.of(
            "number", PluginParamType.NUMBER,
            "string", PluginParamType.STRING,
            "list-of-number", PluginParamType.LIST_OR_SINGLE_NUMBER,
            "list-of-string", PluginParamType.LIST_OR_SINGLE_STRING,
            "list", PluginParamType.LIST_OF_ANY);

    private final Path configPath;
    private final String id;
    private final PluginRole role;
    private final Path templatePath;
    private final TextTemplate compiledTemplate;
    private final Map<String, UserDefinedPluginArgument> arguments;

    UserDefinedPluginConfig(Path configPath,
                            String id,
                            PluginRole role,
                            Path templatePath,
                            TextTemplate compiledTemplate,
                            Map<String, UserDefinedPluginArgument> arguments) {
        this.configPath = configPath;
        this.id = id;
        this.role = role;
        this.templatePath = templatePath;
        this.compiledTemplate = compiledTemplate;
        this.arguments = arguments;
    }

    public static UserDefinedPluginConfig load(ResourcesResolver resourcesResolver, String pluginConfigPath) {
        Path configPath = resourcesResolver.fullPath(pluginConfigPath);
        Map<String, ?> raw = JsonUtils.deserializeAsMap(resourcesResolver.textContent(configPath));
        return parse(resourcesResolver, configPath, raw);
    }

    @SuppressWarnings("unchecked")
    static UserDefinedPluginConfig parse(ResourcesResolver resourcesResolver, Path configPath, Map<String, ?> raw) {
        String label = "plugin config <" + configPath + ">";

        String id = requireString(raw, "id", label);
        PluginRole role = extractRole(raw, label);
        String templateRelative = requireString(raw, "template", label);
        Path templatePath = resourcesResolver.fullPath(templateRelative);

        Map<String, ?> argumentsRaw = (Map<String, ?>) raw.get("arguments");
        if (argumentsRaw == null) {
            argumentsRaw = Collections.emptyMap();
        }

        Map<String, UserDefinedPluginArgument> arguments = new LinkedHashMap<>();
        for (Map.Entry<String, ?> entry : argumentsRaw.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (!(value instanceof Map)) {
                throw new IllegalArgumentException(label + ": argument <" + name + "> must be an object");
            }

            arguments.put(name, parseArgument(resourcesResolver, label, role, name, (Map<String, ?>) value));
        }

        TextTemplate compiledTemplate = new TextTemplate(templatePath.getFileName().toString(),
                resourcesResolver.textContent(templatePath));

        return new UserDefinedPluginConfig(configPath, id, role, templatePath, compiledTemplate, arguments);
    }

    @SuppressWarnings("unchecked")
    private static UserDefinedPluginArgument parseArgument(ResourcesResolver resourcesResolver,
                                                           String label,
                                                           PluginRole role,
                                                           String name,
                                                           Map<String, ?> raw) {
        boolean required = Boolean.TRUE.equals(raw.get("required"));

        if (UserDefinedPluginArgument.FENCE_CONTENT.equals(name)) {
            if (role != PluginRole.FENCE) {
                throw new IllegalArgumentException(label + ": argument <" + UserDefinedPluginArgument.FENCE_CONTENT +
                        "> is only allowed for plugins with <type: fence>");
            }
            return UserDefinedPluginArgument.fenceContent(required);
        }

        if (UserDefinedPluginArgument.FREE_FORM.equals(name)) {
            if (role != PluginRole.INCLUDE) {
                throw new IllegalArgumentException(label + ": argument <" + UserDefinedPluginArgument.FREE_FORM +
                        "> is only allowed for plugins with <type: include>");
            }
            return UserDefinedPluginArgument.freeForm(required);
        }

        String typeId = (String) raw.get("type");
        if (typeId == null) {
            throw new IllegalArgumentException(label + ": argument <" + name + "> is missing <type>");
        }

        PluginParamType baseType = ARGUMENT_TYPES.get(typeId);
        if (baseType == null) {
            throw new IllegalArgumentException("unknown argument type <" + typeId +
                    ">, available: " + String.join(", ", ARGUMENT_TYPES.keySet()));
        }

        Object availableRaw = raw.get("available");
        if (availableRaw == null) {
            return UserDefinedPluginArgument.typed(name, baseType, required);
        }

        List<Object> availableValues;
        Path availablePath = null;

        if (availableRaw instanceof List) {
            availableValues = new ArrayList<>((List<Object>) availableRaw);
        } else if (availableRaw instanceof String reference) {
            if (!reference.startsWith("$")) {
                throw new IllegalArgumentException(label + ": argument <" + name +
                        "> available reference must start with $, got <" + reference + ">");
            }
            availablePath = resourcesResolver.fullPath(reference.substring(1));
            availableValues = new ArrayList<>(JsonUtils.deserializeAsList(resourcesResolver.textContent(availablePath)));
        } else {
            throw new IllegalArgumentException(label + ": argument <" + name +
                    "> available must be a list or a $fileReference");
        }

        PluginParamType paramType = new AvailableValuesParamType(baseType, availableValues);
        return UserDefinedPluginArgument.typed(name, paramType, required, availableValues, availablePath);
    }

    private static PluginRole extractRole(Map<String, ?> raw, String label) {
        Object typeRaw = raw.get("type");
        if (typeRaw == null) {
            throw new IllegalArgumentException(label + ": missing <type>");
        }

        if (!(typeRaw instanceof String typeId)) {
            throw new IllegalArgumentException(label + ": <type> must be a string (one of include, fence)");
        }

        return switch (typeId) {
            case "include" -> PluginRole.INCLUDE;
            case "fence" -> PluginRole.FENCE;
            default -> throw new IllegalArgumentException(label + ": unknown plugin type <" + typeId +
                    ">, available: include, fence");
        };
    }

    private static String requireString(Map<String, ?> raw, String key, String label) {
        Object value = raw.get(key);
        if (!(value instanceof String) || ((String) value).isEmpty()) {
            throw new IllegalArgumentException(label + ": missing required string field <" + key + ">");
        }

        return (String) value;
    }

    public String getId() {
        return id;
    }

    public Path getConfigPath() {
        return configPath;
    }

    public Path getTemplatePath() {
        return templatePath;
    }

    public TextTemplate getCompiledTemplate() {
        return compiledTemplate;
    }

    public Map<String, UserDefinedPluginArgument> getArguments() {
        return arguments;
    }

    public PluginRole getRole() {
        return role;
    }

    public PluginParamsDefinition buildParamsDefinition() {
        PluginParamsDefinition definition = new PluginParamsDefinition();
        for (UserDefinedPluginArgument arg : arguments.values()) {
            if (arg.isFreeForm() || arg.isFenceContent()) {
                continue;
            }

            PluginParamType paramType = arg.getParamType();
            if (arg.isRequired()) {
                definition.addRequired(arg.getName(), paramType, "user-defined argument", paramType.example());
            } else {
                definition.add(arg.getName(), paramType, "user-defined argument", paramType.example());
            }
        }

        return definition;
    }

    public UserDefinedPluginArgument getFreeFormArgument() {
        return arguments.get(UserDefinedPluginArgument.FREE_FORM);
    }

    public UserDefinedPluginArgument getFenceContentArgument() {
        return arguments.get(UserDefinedPluginArgument.FENCE_CONTENT);
    }

    public List<Path> getAvailableValuesPaths() {
        return arguments.values().stream()
                .map(UserDefinedPluginArgument::getAvailableValuesPath)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
