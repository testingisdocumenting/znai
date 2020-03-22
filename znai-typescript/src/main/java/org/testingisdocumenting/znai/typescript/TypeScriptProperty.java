/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.typescript;

import java.util.Map;

public class TypeScriptProperty {
    private String name;
    private String type;
    private String documentation;

    public TypeScriptProperty(String name, String type, String documentation) {
        this.name = name;
        this.type = type;
        this.documentation = documentation;
    }

    public TypeScriptProperty(Map<String, ?> entry) {
        this(entry.get("name").toString(),
                entry.get("type").toString(),
                entry.get("documentation").toString());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDocumentation() {
        return documentation;
    }

    @Override
    public String toString() {
        return "TypeScriptProperty{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", documentation='" + documentation + '\'' +
                '}';
    }
}
