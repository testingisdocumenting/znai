/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.parser;

import org.testingisdocumenting.znai.extensions.footnote.FootnoteId;
import org.testingisdocumenting.znai.extensions.footnote.ParsedFootnote;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MarkdownParsingContext {
    private int footnoteIdx;
    private final Map<FootnoteId, ParsedFootnote> parsedFootnotes = new HashMap<>();
    private final Set<String> footnoteReferences = new LinkedHashSet<>();

    public int nextFootnoteIdx() {
        return ++footnoteIdx;
    }

    public Map<FootnoteId, ParsedFootnote> getParsedFootnotes() {
        return parsedFootnotes;
    }

    public void addFootnoteReference(String label) {
        footnoteReferences.add(label);
    }

    public Set<String> getFootnoteReferences() {
        return footnoteReferences;
    }
}
