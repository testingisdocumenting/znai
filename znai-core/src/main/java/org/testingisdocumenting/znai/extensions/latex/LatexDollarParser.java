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

package org.testingisdocumenting.znai.extensions.latex;

import org.commonmark.parser.beta.*;

import java.util.Set;

public class LatexDollarParser implements InlineContentParser {
    @Override
    public ParsedInline tryParse(InlineParserState state) {
        var scanner = state.scanner();
        scanner.next();

        int numberOfWhiteSpaces = scanner.whitespace();
        if (numberOfWhiteSpaces > 0) {
            return ParsedInline.none();
        }

        char peek = scanner.peek();
        if (peek == '$') {
            return tryParseDoubleDollar(state);
        } else {
            return tryParseSingleDollar(state);
        }
    }

    private ParsedInline tryParseDoubleDollar(InlineParserState state) {
        var scanner = state.scanner();
        scanner.next(); // skip second dollar sign

        var pos = scanner.position();
        boolean metDollar = scanUntilDollarWithNoSpace(state);
        if (!metDollar) {
            return ParsedInline.none();
        }

        char peek = scanner.peek();
        if (peek == '$') {
            var content = scanner.getSource(pos, scanner.position()).getContent();
            scanner.next();
            scanner.next();
            return ParsedInline.of(new LatexDollarBlock(content), scanner.position());
        }

        return ParsedInline.none();
    }

    private ParsedInline tryParseSingleDollar(InlineParserState state) {
        var scanner = state.scanner();
        var pos = scanner.position();
        boolean metDollar = scanUntilDollarWithNoSpace(state);
        if (metDollar) {
            var content = scanner.getSource(pos, scanner.position()).getContent();
            scanner.next();
            return ParsedInline.of(new LatexDollarInline(content), scanner.position());
        } else {
            return ParsedInline.none();
        }
    }

    private boolean scanUntilDollarWithNoSpace(InlineParserState state) {
        var scanner = state.scanner();
        for (;;) {
            var prevChar = scanner.peek();
            if (prevChar == Scanner.END) {
                return false;
            }

            var whitespaceCount = scanner.whitespace();
            var currChar = scanner.peek();

            if (currChar == '$' && whitespaceCount == 0) {
                return true;
            } else {
                scanner.next();
            }
        }
    }

    static public class Factory implements InlineContentParserFactory {
        @Override
        public Set<Character> getTriggerCharacters() {
            return Set.of('$');
        }

        @Override
        public InlineContentParser create() {
            return new LatexDollarParser();
        }
    }
}
