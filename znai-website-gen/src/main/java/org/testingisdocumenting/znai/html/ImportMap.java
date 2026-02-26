/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.html;

import org.testingisdocumenting.znai.website.WebResource;

import java.util.Map;
import java.util.stream.Collectors;

public class ImportMap {
    private final Map<String, WebResource> map;
    public ImportMap(Map<String, String> map) {
        this.map = map.entrySet().stream().map(e -> Map.entry(e.getKey(), WebResource.fromResource(e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public String render(String documentationId) {
        String imports = map.entrySet().stream()
                .map(entry -> "        \"" + entry.getKey() + "\": \"" + entry.getValue().pathForHtml(documentationId) + "\"")
                .collect(Collectors.joining(",\n"));

        return "<script type=\"importmap\">\n" +
                "    {\n" +
                "      \"imports\": {\n" +
                imports +
                "      }\n" +
                "    }\n" +
                "</script>";
    }
}
