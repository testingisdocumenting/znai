/*
 * Copyright 2023 znai maintainers
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

class RegionScopeExtractor {
    private final String[] lines;
    private final int startLineIdx;
    private final char scopeStart;
    private final char scopeEnd;
    private boolean insideSingleQuote;
    private boolean insideDoubleQuote;
    private int resultStartLineIdx;
    private int resultEndLineIdx;

    RegionScopeExtractor(String[] lines, int startLineIdx, char scopeStart, char scopeEnd) {
        this.lines = lines;
        this.startLineIdx = startLineIdx;
        this.scopeStart = scopeStart;
        this.scopeEnd = scopeEnd;
    }

    public int getResultStartLineIdx() {
        return resultStartLineIdx;
    }

    public int getResultEndLineIdx() {
        return resultEndLineIdx;
    }

    void process() {
        int scopeBalance = 0;
        boolean encounteredScopeStart = false;
        resultStartLineIdx = startLineIdx;
        resultEndLineIdx = -1;

        for (int lineIdx = startLineIdx; lineIdx < lines.length; lineIdx++) {
            String line = lines[lineIdx];

            char previousChar = ' ';
            for (int charIdx = 0; charIdx < line.length(); charIdx++) {
                char c = line.charAt(charIdx);

                if (!insideSingleQuote && !insideDoubleQuote) {
                    if (c == scopeStart) {
                        encounteredScopeStart = true;
                        scopeBalance++;
                    } else if (c == scopeEnd) {
                        scopeBalance--;
                        if (scopeBalance == 0 && encounteredScopeStart) {
                            resultEndLineIdx = lineIdx;
                            return;
                        }
                    }
                }

                boolean isPreviousCharEscape = previousChar == '\\';
                if (c == '\"' && !isPreviousCharEscape) {
                    insideDoubleQuote = !insideDoubleQuote;
                }

                if (c == '\'' && !isPreviousCharEscape) {
                    insideSingleQuote = !insideSingleQuote;
                }

                previousChar = c;
            }
        }
    }
}
