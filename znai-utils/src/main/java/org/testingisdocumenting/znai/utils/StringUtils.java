/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.utils;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class StringUtils {
    private StringUtils() {
    }

    public static String nullAsEmpty(Object v) {
        return v == null ? "" : v.toString();
    }

    public static int maxLineLength(String text) {
        return Arrays.stream(text.replace("\r", "").split("\n")).
                map(String::length).
                max(Integer::compareTo).
                orElse(0);
    }

    public static String stripIndentation(String text) {
        List<String> lines = trimEmptyLines(Arrays.asList(text.replace("\r", "").split("\n")));
        Integer indentation = lines.stream().
                filter(StringUtils::notEmptyLine).
                map(StringUtils::lineIndentation).min(Integer::compareTo).orElse(0);

        return lines.stream().map(l -> removeIndentation(l, indentation)).collect(Collectors.joining("\n"));
    }

    public static String extractInsideCurlyBraces(String code) {
        int startIdx = code.indexOf('{');
        if (startIdx == -1) {
            return "";
        }

        int endIdx = code.lastIndexOf('}');

        return code.substring(startIdx + 1, endIdx);
    }

    public static String removeContentInsideBracketsInclusive(String code) {
        int openIdx = code.indexOf('<');
        return openIdx == -1 ? code : code.substring(0, openIdx);
    }

    private static Integer lineIndentation(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }

        return i;
    }

    public static String createIndentation(int numberOfSpaces) {
        return numberOfSpaces == 0 ? "" : String.format("%" + numberOfSpaces + "s", "");
    }

    public static String concatWithIndentation(String prefix, String multilineText) {
        String indentation = StringUtils.createIndentation(prefix.length());

        String[] lines = multilineText.split("\n");
        return (prefix + lines[0]) + (lines.length > 1 ?
                "\n" + Arrays.stream(lines).skip(1).map(l -> indentation + l).collect(joining("\n")) :
                "");
    }

    public static String removeQuotes(String text) {
        if ((text.startsWith("\"") || text.startsWith("'")) &&
                text.endsWith("\"") || text.endsWith("'")) {
            return text.substring(1, text.length() - 1);
        }

        return text;
    }

    public static String wrapInDoubleQuotes(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text;
        }

        return "\"" + text + "\"";
    }

    public static boolean isNumeric(NumberFormat numberFormat, String text) {
        text = text.trim();

        if (text.isEmpty()) {
            return false;
        }

        ParsePosition pos = new ParsePosition(0);
        numberFormat.parse(text, pos);

        return text.length() == pos.getIndex();
    }

    private static String removeIndentation(String line, Integer indentation) {
        if (line.trim().isEmpty()) {
            return line;
        }

        return line.substring(indentation);
    }

    private static boolean notEmptyLine(String s) {
        return ! s.trim().isEmpty(); // TODO replace with more pragmatic impl
    }

    private static List<String> trimEmptyLines(List<String> lines) {
        int b = firstNonEmptyLineIdx(lines);
        int e = firstFromEndNonEmptyLineIdx(lines);

        return lines.subList(Math.max(b, 0), e > 0 ? e + 1 : lines.size());
    }

    private static int firstNonEmptyLineIdx(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (notEmptyLine(lines.get(i))) {
                return i;
            }
        }

        return -1;
    }

    private static int firstFromEndNonEmptyLineIdx(List<String> lines) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (notEmptyLine(lines.get(i))) {
                return i;
            }
        }

        return -1;
    }
}
