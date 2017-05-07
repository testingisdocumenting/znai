package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.twosigma.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.twosigma.utils.StringUtils.extractInsideCurlyBraces;
import static com.twosigma.utils.StringUtils.stripIndentation;

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
        if (! detailsByName.containsKey(methodName)) {
            throw new RuntimeException("no method found: " + methodName);
        }

        return detailsByName.get(methodName);
    }

    public String getTopLevelJavaDoc() {
        return topLevelJavaDoc;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, String arg) {
        JavadocComment javadocComment = n.getJavadocComment();

        topLevelJavaDoc = (javadocComment != null) ?
                javadocComment.parse().getDescription().toText() :
                "";

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
        String methodJavaDoc = (javadocComment != null) ?
                javadocComment.parse().getDescription().toText() :
                "";

        detailsByName.put(name, new JavaMethodDetails(
                stripIndentation(code),
                stripIndentation(extractInsideCurlyBraces(code)),
                methodJavaDoc));
    }
}
