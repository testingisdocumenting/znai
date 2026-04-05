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

package org.testingisdocumenting.znai.utils;

import java.util.ArrayList;
import java.util.List;

public class TextLineWrapper {
    public static String wrapLine(String line, int maxWidth) {
        if (line == null || line.isEmpty() || line.length() <= maxWidth) {
            return line;
        }

        List<String> tokens = tokenize(line);
        StringBuilder result = new StringBuilder();
        int currentLineLength = 0;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (isWhitespace(token)) {
                currentLineLength = appendWhitespaceOrWrap(tokens, i, result, currentLineLength, maxWidth);
            } else {
                result.append(token);
                currentLineLength += token.length();
            }
        }

        return result.toString();
    }

    private static int appendWhitespaceOrWrap(List<String> tokens, int whitespaceIdx,
                                              StringBuilder result, int currentLineLength, int maxWidth) {
        String whitespace = tokens.get(whitespaceIdx);
        String nextWord = getNextWordOrEmpty(tokens, whitespaceIdx);
        int lengthIfKeptOnSameLine = currentLineLength + whitespace.length() + nextWord.length();

        if (lengthIfKeptOnSameLine <= maxWidth) {
            result.append(whitespace);
            return currentLineLength + whitespace.length();
        }

        result.append("\n");
        return 0;
    }

    private static String getNextWordOrEmpty(List<String> tokens, int currentIdx) {
        int nextIdx = currentIdx + 1;
        return nextIdx < tokens.size() ? tokens.get(nextIdx) : "";
    }

    private static boolean isWhitespace(String token) {
        return token.charAt(0) == ' ';
    }

    /**
     * Splits line into alternating sequences of whitespace and non-whitespace.
     * Example: "hello  world" -> ["hello", "  ", "world"]
     */
    private static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean collectingWhitespace = line.charAt(0) == ' ';

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            boolean charIsWhitespace = c == ' ';

            if (charIsWhitespace == collectingWhitespace) {
                currentToken.append(c);
            } else {
                tokens.add(currentToken.toString());
                currentToken = new StringBuilder();
                currentToken.append(c);
                collectingWhitespace = charIsWhitespace;
            }
        }

        if (!currentToken.isEmpty()) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }
}
