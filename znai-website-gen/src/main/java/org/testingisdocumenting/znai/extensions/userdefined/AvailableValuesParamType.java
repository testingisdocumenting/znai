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
import org.testingisdocumenting.znai.extensions.paramtypes.PluginParamTypeListOfAny;
import org.testingisdocumenting.znai.extensions.paramtypes.PluginParamTypeListOrSingleNumber;
import org.testingisdocumenting.znai.extensions.paramtypes.PluginParamTypeListOrSingleString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class AvailableValuesParamType implements PluginParamType {
    private final PluginParamType baseType;
    private final List<Object> availableValues;
    private final Set<String> availableValuesAsStrings;
    private final String description;
    private final String example;

    AvailableValuesParamType(PluginParamType baseType, List<Object> availableValues) {
        this.baseType = baseType;
        this.availableValues = availableValues;
        this.availableValuesAsStrings = availableValues.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());

        String renderedValues = availableValues.stream()
                .map(AvailableValuesParamType::renderValue)
                .collect(Collectors.joining(", "));
        this.description = baseType.description() + " of " + renderedValues;

        String firstRendered = renderValue(availableValues.get(0));
        this.example = isListBaseType(baseType) ? "[" + firstRendered + "]" : firstRendered;
    }

    public List<Object> getAvailableValues() {
        return availableValues;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String example() {
        return example;
    }

    @Override
    public boolean isValid(Object param) {
        if (!baseType.isValid(param)) {
            return false;
        }

        if (param instanceof List) {
            return ((List<?>) param).stream().allMatch(this::matchesAvailable);
        }

        return matchesAvailable(param);
    }

    private boolean matchesAvailable(Object value) {
        return availableValuesAsStrings.contains(value.toString());
    }

    private static boolean isListBaseType(PluginParamType baseType) {
        return baseType instanceof PluginParamTypeListOrSingleNumber
                || baseType instanceof PluginParamTypeListOrSingleString
                || baseType instanceof PluginParamTypeListOfAny;
    }

    private static String renderValue(Object value) {
        if (value instanceof CharSequence) {
            return "\"" + value + "\"";
        }

        return value.toString();
    }
}
