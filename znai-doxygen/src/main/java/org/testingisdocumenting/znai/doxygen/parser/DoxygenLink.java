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

package org.testingisdocumenting.znai.doxygen.parser;

import java.util.HashMap;
import java.util.Map;

public class DoxygenLink {
    private final String text;
    private final String refId;

    public DoxygenLink(String text, String refId) {
        this.text = text;
        this.refId = refId;
    }

    public String getText() {
        return text;
    }

    public String getRefId() {
        return refId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("refId", refId);

        return result;
    }
}
