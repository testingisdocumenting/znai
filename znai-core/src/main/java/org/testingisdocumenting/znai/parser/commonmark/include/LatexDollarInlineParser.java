/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.parser.commonmark.include;

import org.commonmark.parser.beta.InlineContentParser;
import org.commonmark.parser.beta.InlineContentParserFactory;
import org.commonmark.parser.beta.InlineParserState;
import org.commonmark.parser.beta.ParsedInline;

import java.util.Set;

public class LatexDollarInlineParser implements InlineContentParser {
    @Override
    public ParsedInline tryParse(InlineParserState inlineParserState) {
        var scanner = inlineParserState.scanner();
        scanner.next();
        var pos = scanner.position();

        var end = scanner.find('$');
        if (end == -1) {
            return ParsedInline.none();
        }
        
        var content = scanner.getSource(pos, scanner.position()).getContent();
        scanner.next();
        return ParsedInline.of(new LatexDollarInline(content), scanner.position());
    }

    static public class Factory implements InlineContentParserFactory {
        @Override
        public Set<Character> getTriggerCharacters() {
            return Set.of('$');
        }

        @Override
        public InlineContentParser create() {
            return new LatexDollarInlineParser();
        }
    }
}
