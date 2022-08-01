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

public class OpenApi3Request {
    private final String description;

    private final OpenApi3Content content;

    public OpenApi3Request(String description, OpenApi3Content content) {
        this.description = description;
        this.content = content;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("description", description);

        Map<String, Object> contentMap = new LinkedHashMap<>();
        content.getByMimeType().forEach((type, schema) -> contentMap.put(type, schema.toMap()));

        result.put("content", contentMap);

        return result;
    }
}
