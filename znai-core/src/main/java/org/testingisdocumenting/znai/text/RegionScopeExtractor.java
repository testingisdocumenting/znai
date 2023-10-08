/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.text;

public class RegionScopeExtractor {
    private final int startLineIdx;
    private String scopeStart;
    private String scopeEnd;
    private final TextLinesAccessor linesAccessor;
    private boolean insideSingleQuote;
    private boolean insideDoubleQuote;
    private boolean isSingleCharScope;
    private int resultStartLineIdx;
    private int resultEndLineIdx;

    public RegionScopeExtractor(TextLinesAccessor linesAccessor, int startLineIdx, String scopeDefinition) {
        this.linesAccessor = linesAccessor;
        this.startLineIdx = startLineIdx;
        extractScope(scopeDefinition);
    }

    public int getResultStartLineIdx() {
        return resultStartLineIdx;
    }

    public int getResultEndLineIdx() {
        return resultEndLineIdx;
    }

    public void process() {
        int scopeBalance = 0;
        boolean encounteredScopeStart = false;
        resultStartLineIdx = -1;
        resultEndLineIdx = -1;

        for (int lineIdx = startLineIdx; lineIdx < linesAccessor.numberOfLines(); lineIdx++) {
            String line = linesAccessor.lineAtIdx(lineIdx);

            char previousChar = ' ';
            for (int charIdx = 0; charIdx < line.length(); charIdx++) {
                char c = line.charAt(charIdx);

                if (!insideSingleQuote && !insideDoubleQuote) {
                    boolean scopeStartMatch = matchSubstr(line, charIdx, scopeStart);
                    boolean scopeEndMatch = matchSubstr(line, charIdx, scopeEnd);
                    if (scopeStartMatch) {
                        charIdx += scopeStart.length() - 1;
                        if (!encounteredScopeStart) {
                            resultStartLineIdx = Math.min(startLineIdx, lineIdx);
                            encounteredScopeStart = true;
                        }
                        scopeBalance++;
                    } else if (scopeEndMatch) {
                        charIdx += scopeEnd.length() - 1;
                        scopeBalance--;
                        if (scopeBalance < 0) {
                            return;
                        }
                        else if (scopeBalance == 0) {
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

    private boolean matchSubstr(String line, int charIdx, String substr) {
        if (isSingleCharScope) {
            return line.charAt(charIdx) == substr.charAt(0);
        }

        int idx = charIdx;
        int lineLen = line.length();
        int subLen = substr.length();

        if (idx + subLen > lineLen) {
            return false;
        }

        if (idx > 0 && !isSeparator(line.charAt(idx - 1))) {
            return false;
        }

        for (int subIdx = 0; subIdx < subLen && idx < lineLen; subIdx++, idx++) {
            char sourceC = line.charAt(idx);
            char subC = substr.charAt(subIdx);
            if (sourceC != subC) {
                return false;
            }
        }

        return idx >= lineLen || isSeparator(line.charAt(idx));
    }

    private boolean isSeparator(char c) {
        return !Character.isAlphabetic(c) && !Character.isDigit(c);
    }

    private void extractScope(String scopeDefinition) {
        if (scopeDefinition.length() == 2) {
            scopeStart = String.valueOf(scopeDefinition.charAt(0));
            scopeEnd = String.valueOf(scopeDefinition.charAt(1));
            isSingleCharScope = true;
        } else if (scopeDefinition.contains(",") && scopeDefinition.length() > 2) {
            int commaIdx = scopeDefinition.indexOf(',');
            scopeStart = scopeDefinition.substring(0, commaIdx);
            scopeEnd = scopeDefinition.substring(commaIdx + 1);
            isSingleCharScope = scopeStart.length() == 1 && scopeEnd.length() == 1;
        } else {
            throw new IllegalArgumentException("scope should be defined either as two chars like \"{}\", \"[]\"," +
                    " or comma separated two strings \"sig,end\", \"let,in\"");
        }
    }
}
