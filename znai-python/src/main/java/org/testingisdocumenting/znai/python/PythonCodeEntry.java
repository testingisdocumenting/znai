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

package org.testingisdocumenting.znai.python;

import java.util.Map;
import java.util.Objects;

public class PythonCodeEntry {
    private final String name;
    private final String type;
    private final String content;
    private final String bodyOnly;
    private final String docString;

    public PythonCodeEntry(Map<String, Object> parsed) {
        this.name = Objects.toString(parsed.get("name"), "");
        this.type = Objects.toString(parsed.get("type"), "");
        this.content = Objects.toString(parsed.get("content"), "");
        this.bodyOnly = Objects.toString(parsed.get("body_only"), "");
        this.docString = Objects.toString(parsed.get("doc_string"), "");
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public String getDocString() {
        return docString;
    }
}
