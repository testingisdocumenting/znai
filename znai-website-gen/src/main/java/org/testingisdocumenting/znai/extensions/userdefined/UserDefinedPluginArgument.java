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

import java.nio.file.Path;
import java.util.List;

public class UserDefinedPluginArgument {
    public static final String FREE_FORM = "freeForm";
    public static final String FENCE_CONTENT = "fenceContent";

    private final String name;
    private final PluginParamType paramType;
    private final boolean required;
    private final List<Object> availableValues;
    private final Path availableValuesPath;

    private UserDefinedPluginArgument(String name,
                                      PluginParamType paramType,
                                      boolean required,
                                      List<Object> availableValues,
                                      Path availableValuesPath) {
        this.name = name;
        this.paramType = paramType;
        this.required = required;
        this.availableValues = availableValues;
        this.availableValuesPath = availableValuesPath;
    }

    public static UserDefinedPluginArgument freeForm(boolean required) {
        return new UserDefinedPluginArgument(FREE_FORM, null, required, null, null);
    }

    public static UserDefinedPluginArgument fenceContent(boolean required) {
        return new UserDefinedPluginArgument(FENCE_CONTENT, null, required, null, null);
    }

    public static UserDefinedPluginArgument typed(String name, PluginParamType paramType, boolean required) {
        return new UserDefinedPluginArgument(name, paramType, required, null, null);
    }

    public static UserDefinedPluginArgument typed(String name,
                                                  PluginParamType paramType,
                                                  boolean required,
                                                  List<Object> availableValues,
                                                  Path availableValuesPath) {
        return new UserDefinedPluginArgument(name, paramType, required, availableValues, availableValuesPath);
    }

    public String getName() {
        return name;
    }

    public PluginParamType getParamType() {
        return paramType;
    }

    public boolean isRequired() {
        return required;
    }

    public List<Object> getAvailableValues() {
        return availableValues;
    }

    public Path getAvailableValuesPath() {
        return availableValuesPath;
    }

    public boolean isFreeForm() {
        return FREE_FORM.equals(name);
    }

    public boolean isFenceContent() {
        return FENCE_CONTENT.equals(name);
    }
}
