package com.twosigma.documentation.cpp;

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
        List<String> lines = Arrays.asList(code.replace("\r", "").split("\n"));
        Integer indentation = lines.stream().
                filter(CodeSnippetsUtils::notEmptyLine).
                map(CodeSnippetsUtils::lineIndentation).min(Integer::compareTo).orElse(0);

        return lines.stream().map(l -> removeIndentation(l, indentation)).collect(Collectors.joining("\n")).trim()  ;
    }

    private static String removeIndentation(String line, Integer indentation) {
        if (line.trim().isEmpty()) {
            return line;
        }

        return line.substring(indentation);
    }

    private static boolean notEmptyLine(String s) {
        return ! s.trim().isEmpty();
    }

    private static Integer lineIndentation(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }

        return i;
    }
}
