package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.BodyDeclaration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class JavaCodeUtils {
    public static String removeSemicolonAtEnd(String code) {
        return code.endsWith(";") ? code.substring(0, code.length() - 1) : code;
    }

    public static String removeReturn(String code) {
        return code.replace("return", "      ");
    }

    static String extractCode(List<String> lines, BodyDeclaration declaration) {
        Range range = declaration.getRange().orElseGet(() -> new Range(new Position(0, 0), new Position(0, 0)));
        int startLine = range.begin.line - 1;
        int endLine = range.end.line - 1;

        return lines.subList(startLine, endLine + 1).stream().collect(Collectors.joining("\n"));
    }

    static String extractSignature(String code) {
        int i = code.indexOf('{');
        return i == -1 ? code.trim() : code.substring(0, i).trim();
    }
}
