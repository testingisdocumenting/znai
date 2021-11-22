/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiLinkedText {
    private final List<ApiLinkedTextPart> parts;

    public ApiLinkedText() {
        this.parts = new ArrayList<>();
    }

    public ApiLinkedText(String textOnly) {
        this.parts = new ArrayList<>();
        if (!textOnly.trim().isEmpty()) {
            addPart(textOnly);
        }
    }

    public void addPart(String text) {
        parts.add(new ApiLinkedTextPart(text, ""));
    }

    public void addPart(String text, String refId) {
        parts.add(new ApiLinkedTextPart(text, refId));
    }

    public List<ApiLinkedTextPart> getParts() {
        return parts;
    }

    public List<Map<String, Object>> toListOfMaps() {
        return parts.stream().map(ApiLinkedTextPart::toMap).collect(Collectors.toList());
    }

    public String buildCombinedText() {
        return this.parts.stream()
                .map((part) -> part.getText().trim())
                .collect(Collectors.joining(" "));
    }
}
