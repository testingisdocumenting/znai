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

public enum PluginParamType {
    STRING("string", "\"text\""),
    NUMBER("number", "5"),
    BOOLEAN("boolean", "true"),
    OBJECT("object", "{nestedField: true}"),
    STRING_OR_NUMBER("string or number", "\"text\" or 5"),
    LIST_OR_SINGLE_NUMBER("list or a single number value", "5 or [5, 8]"),
    LIST_OR_SINGLE_STRING_OR_NUMBER("list or a single value of either number(s) or string(s)", "\"hello\" or [\"text\", 8]"),
    LIST_OR_SINGLE_STRING("list or a single value of string(s)", "\"hello\" or [\"hello\", \"text\"]"),
    LIST_OR_SINGLE_STRING_WITH_NULLS("list or a single value of string(s) with optional null values", "\"hello\" or [null, \"text\"]"),
    LIST_OF_ANY("list", "[\"hello\", \"text\"]");

    private final String description;
    private final String example;

    PluginParamType(String description, String example) {
        this.description = description;
        this.example = example;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "<" + description + "> (e.g. " + example + ")";
    }
}