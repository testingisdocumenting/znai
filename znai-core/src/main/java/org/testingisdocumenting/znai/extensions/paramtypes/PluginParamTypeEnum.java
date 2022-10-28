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

package org.testingisdocumenting.znai.extensions.paramtypes;

import org.testingisdocumenting.znai.extensions.PluginParamType;

import java.util.List;
import java.util.stream.Collectors;

public class PluginParamTypeEnum implements PluginParamType {
    private final List<String> values;

    public PluginParamTypeEnum(List<String> values) {
        this.values = values;
    }

    @Override
    public String description() {
        return "enum " + values.stream().map(v -> "\"" + v + "\"").collect(Collectors.joining(", "));
    }

    @Override
    public String example() {
        return "\"" + values.get(0) + "\"";
    }

    @Override
    public boolean isValid(Object param) {
        return values.contains(param.toString());
    }
}
