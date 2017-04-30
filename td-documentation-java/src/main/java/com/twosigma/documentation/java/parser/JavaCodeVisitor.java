package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.twosigma.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class JavaCodeVisitor extends VoidVisitorAdapter<String> {
    private final List<String> lines;
    private Map<String, JavaMethodDetails> detailsByName;
    private String topLevelJavaDoc;

    public JavaCodeVisitor(String code) {
        lines = Arrays.asList(code.split("\n"));
        detailsByName = new HashMap<>();
    }

    public JavaMethodDetails getDetails(String methodName) {
        return detailsByName.get(methodName);
    }

    public String getTopLevelJavaDoc() {
        return topLevelJavaDoc;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, String arg) {
        JavadocComment javadocComment = n.getJavadocComment();

        Javadoc javadoc = javadocComment.parse();
        topLevelJavaDoc = javadoc.getDescription().toText();

        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, String arg) {
        Range range = methodDeclaration.getRange().orElseGet(() -> new Range(new Position(0, 0), new Position(0, 0)));
        int startLine = range.begin.line - 1;
        int endLine = range.end.line - 1;

        String name = methodDeclaration.getName().getIdentifier();
        String code = lines.subList(startLine, endLine + 1).stream().collect(Collectors.joining("\n"));

        JavadocComment javadocComment = methodDeclaration.getJavadocComment();
        Javadoc javadoc = javadocComment.parse();

        detailsByName.put(name, new JavaMethodDetails(
                StringUtils.stripIndentation(code),
                removeSignatureAndReIndent(code),
                javadoc.getDescription().toText()));
    }

    private String removeSignatureAndReIndent(String code) {
        int startIdx = code.indexOf('{');
        int endIdx = code.lastIndexOf('}');

        String insideBraces = code.substring(startIdx + 1, endIdx - 1);
        return StringUtils.stripIndentation(insideBraces);
    }
}
