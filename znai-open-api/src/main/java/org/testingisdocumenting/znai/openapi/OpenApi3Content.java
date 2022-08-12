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

package org.testingisdocumenting.znai.openapi;

import java.util.LinkedHashMap;
import java.util.Map;

public class OpenApi3Content {
    private final Map<String, OpenApi3Schema> schemaByMimeType;
    private final Map<String, String> exampleByMimeType;

    public OpenApi3Content() {
        this.schemaByMimeType = new LinkedHashMap<>();
        this.exampleByMimeType = new LinkedHashMap<>();
    }

    public void register(String mimeType, OpenApi3Schema schema, Object example) {
        schemaByMimeType.put(mimeType, schema);
        if (example != null) {
            exampleByMimeType.put(mimeType, example.toString());
        }
    }

    public Map<String, OpenApi3Schema> getSchemaByMimeType() {
        return schemaByMimeType;
    }

    public String exampleByMimeType(String mimeType) {
        return exampleByMimeType.getOrDefault(mimeType, "");
    }

    public boolean isEmpty() {
        return schemaByMimeType.isEmpty();
    }
}
