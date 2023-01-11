/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.file;

import java.util.ArrayList;
import java.util.List;

/**
 * convert and validates idx or contains into a list of indexes. Used for example for text highlight.
 */
public class SnippetContainerEntriesConverter {
    private final String[] lines;
    private final String label;
    private final String content;
    private final String snippetId;

    public SnippetContainerEntriesConverter(String snippetId, String content, String label) {
        this.label = label;
        this.content = content;
        this.snippetId = snippetId;
        this.lines = content.split("\n");
    }

    public List<Integer> convertAndValidate(List<Object> idxOrContains) {
        List<Integer> result = new ArrayList<>();

        for (Object idxOrText : idxOrContains) {
            if (idxOrText instanceof Number) {
                result.add(validateIdx((Number) idxOrText));
            } else {
                result.addAll(validateContainsAndGetIdx((String) idxOrText));
            }
        }

        return result;
    }

    public int validateIdx(Number idx) {
        int idxInt = idx.intValue();
        if (idxInt >= lines.length || idxInt < 0) {
            throw new IllegalArgumentException(label + " idx is out of range: " + idx + exceptionIdMessage());
        }

        return idxInt;
    }

    public List<Integer> findAllContainsIdx(String partial) {
        List<Integer> result = new ArrayList<>();
        int idx = 0;
        for (String line : lines) {
            if (line.contains(partial)) {
                result.add(idx);
            }
            idx++;
        }

        return result;
    }

    public List<Integer> validateContainsAndGetIdx(String partial) {
        List<Integer> containsIndexes = findAllContainsIdx(partial);
        if (containsIndexes.isEmpty()) {
            throw new IllegalArgumentException(label + " text <" + partial + "> is not found" +
                    exceptionIdMessage() +
                    "\n" + content);
        }

        return containsIndexes;
    }

    private String exceptionIdMessage() {
        if (snippetId.isEmpty()) {
            return "";
        }

        return "\ncheck: " + snippetId;
    }
}
