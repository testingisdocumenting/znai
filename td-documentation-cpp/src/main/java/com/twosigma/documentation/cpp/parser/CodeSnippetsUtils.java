package com.twosigma.documentation.cpp.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class CodeSnippetsUtils {
    private CodeSnippetsUtils() {
    }

    public static String stripIndentation(String code) {
        List<String> lines = trimEmptyLines(Arrays.asList(code.replace("\r", "").split("\n")));
        Integer indentation = lines.stream().
                filter(CodeSnippetsUtils::notEmptyLine).
                map(CodeSnippetsUtils::lineIndentation).min(Integer::compareTo).orElse(0);

        return lines.stream().map(l -> removeIndentation(l, indentation)).collect(Collectors.joining("\n"));
    }

    public static Integer lineIndentation(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }

        return i;
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

        return lines.subList(b > 0 ? b : 0, e > 0 ? e + 1 : lines.size());
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
