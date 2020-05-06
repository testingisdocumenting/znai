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

package org.testingisdocumenting.znai.openapi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OpenApiParameter {
    private String name;
    private String in;
    private String type;
    private boolean required;
    private Map<String, ?> schema;
    private List<Map<String, Object>> description;

    public OpenApiParameter(String name, String in, String type, boolean required,
                            Map<String, ?> schema,
                            List<Map<String, Object>> description) {
        this.name = name;
        this.in = in;
        this.type = type;
        this.required = required;
        this.schema = schema;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getIn() {
        return in;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public List<Map<String, Object>> getDescription() {
        return description;
    }

    public Map<String, ?> getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "OpenApiParameter{" +
                "name='" + name + '\'' +
                ", in='" + in + '\'' +
                ", type='" + type + '\'' +
                ", required=" + required +
                ", schema=" + schema +
                ", description=" + description +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("in", in);
        result.put("type", type);
        result.put("required", required);
        result.put("schema", schema);
        result.put("description", description);

        return result;
    }
}
