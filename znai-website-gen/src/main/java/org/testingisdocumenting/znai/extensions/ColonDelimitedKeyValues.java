/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Represents key values extracted from the text of key: value1 format
 * <pre>
 * key1:value1
 * key2:value21
 * value22
 * key3: value3
 * key4:
 * value41
 * value42
 * </pre>
 */
public class ColonDelimitedKeyValues {
    private final String[] lines;
    private final Map<String, String> vars;
    private String currentVarName;
    private final List<String> currentValueLines;

    public ColonDelimitedKeyValues(String content) {
        lines = content.split("\n");
        vars = new LinkedHashMap<>();
        currentVarName = "";
        currentValueLines = new ArrayList<>();
        extractVars();
    }

    public void forEach(BiConsumer<String, String> consumer) {
        vars.forEach(consumer);
    }

    public <R> Stream<R> map(BiFunction<String, String, R> function) {
        return vars.entrySet().stream().map(e -> function.apply(e.getKey(), e.getValue()));
    }

    public String get(String name) {
        if (!vars.containsKey(name)) {
            throw new RuntimeException("no value defined for key: " + name);
        }

        return vars.get(name);
    }

    public boolean has(String name) {
        return vars.containsKey(name);
    }

    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(vars);
    }

    @Override
    public String toString() {
        return vars.toString();
    }

    private void extractVars() {
        Arrays.stream(lines).forEach(this::processLine);
        flushVar();
    }

    private void processLine(String line) {
        KeyValuePart keyValuePart = extractKeyAndValuePart(line);

        if (! keyValuePart.key.isEmpty()) {
            flushVar();

            currentVarName = keyValuePart.key;
            if (! keyValuePart.valuePart.trim().isEmpty()) {
                currentValueLines.add(keyValuePart.valuePart);
            }
        } else {
            currentValueLines.add(line);
        }
    }

    private static KeyValuePart extractKeyAndValuePart(String line) {
        if (line.startsWith(" ")) {
            return new KeyValuePart("", line);
        }

        line = line.trim();

        char pc = ' ';

        int quoteStartIdx = -1;
        int quoteEndIdx = -1;
        int numberOfQuotes = 0;
        int numberOfDelimitersOutsideQuote = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            boolean isQuote = c == '"' && pc != '\\';
            if (isQuote) {
                if (quoteStartIdx == -1) {
                    quoteStartIdx = i;
                } else if (quoteEndIdx == -1) {
                    quoteEndIdx = i;
                }

                numberOfQuotes++;
            }

            if (c == ':') {
                if (numberOfQuotes == 2 && numberOfDelimitersOutsideQuote == 0) {
                    return new KeyValuePart(line.substring(quoteStartIdx + 1, quoteEndIdx), line.substring(quoteEndIdx + 2));
                }

                if (numberOfQuotes == 0 && numberOfDelimitersOutsideQuote == 0) {
                    return new KeyValuePart(line.substring(0, i), line.substring(i + 1));
                }
            } else if (numberOfQuotes % 2 == 0 && Character.isSpaceChar(c) || c == '[') {
                numberOfDelimitersOutsideQuote++;
            }

            pc = c;
        }

        return new KeyValuePart("", line);
    }

    private void flushVar() {
        if (currentVarName.isEmpty()) {
            return;
        }

        vars.put(currentVarName, String.join("\n", currentValueLines));

        currentValueLines.clear();
        currentVarName = "";
    }

    private static class KeyValuePart {
        private final String key;
        private final String valuePart;

        KeyValuePart(String key, String valuePart) {
            this.key = key;
            this.valuePart = valuePart;
        }
    }
}
