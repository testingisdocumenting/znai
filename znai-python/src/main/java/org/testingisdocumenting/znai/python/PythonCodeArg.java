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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.structure.DocStructure;

import java.util.LinkedHashMap;
import java.util.Map;

public class PythonCodeArg {
    enum Category {
        REGULAR, // regular params
        POS_ONLY, // params before `,/ ,
        KW_ONLY, // key=value after *args
        ARGS,  // *names
        KWARGS // **opts
    }

    private final Category category;
    private final String name;
    private final PythonCodeType type;
    private final String defaultValue;

    public PythonCodeArg(Map<String, Object> parsed, String defaultPackageName) {
        this.category = extractCategory(parsed.get("category").toString());
        this.name = parsed.get("name").toString();
        this.type = new PythonCodeType(parsed.get("type"), defaultPackageName);
        this.defaultValue = parsed.containsKey("defaultValue") ? parsed.get("defaultValue").toString() : "";
    }

    public String getName() {
        return name;
    }

    public PythonCodeType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "PythonCodeArg{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    public Map<String, ?> toMap(DocStructure docStructure) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("category", category.name());
        map.put("name", name);
        map.put("defaultValue", defaultValue);
        map.put("type", type.convertToApiLinkedText(docStructure).toListOfMaps());

        return map;
    }

    private Category extractCategory(String category) {
        switch (category) {
            case "args":
                return Category.ARGS;
            case "kwargs":
                return Category.KWARGS;
            case "pos_only":
                return Category.POS_ONLY;
            case "kw_only":
                return Category.KW_ONLY;
            case "regular":
            default:
                return Category.REGULAR;
        }
    }
}
