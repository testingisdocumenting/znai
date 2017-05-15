package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.javaparser.javadoc.JavadocBlockTag.Type.PARAM;
import static com.twosigma.utils.StringUtils.extractInsideCurlyBraces;
import static com.twosigma.utils.StringUtils.stripIndentation;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author mykola
 */
public class JavaCodeVisitor extends VoidVisitorAdapter<String> {
    private final List<String> lines;
    private List<JavaMethod> javaMethods;
    private String topLevelJavaDoc;

    public JavaCodeVisitor(String code) {
        lines = Arrays.asList(code.split("\n"));
        javaMethods = new ArrayList<>();
    }

    public JavaMethod getDetails(String methodName) {
        return javaMethods.stream().filter(m -> m.getName().equals(methodName)).findFirst()
                .orElseThrow(() -> new RuntimeException("no method found: " + methodName));
    }

    public JavaMethod getDetails(String methodName, List<String> paramNames) {
        return javaMethods.stream().filter(m -> m.getName().equals(methodName) && m.getParamNames().equals(paramNames))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no method found: " + methodName + " with params: " + paramNames));
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
        Javadoc javadoc = javadocComment != null ? javadocComment.parse() : null;

        String javaDocText = (javadoc != null) ?
                javadoc.getDescription().toText() :
                "";

        javaMethods.add(new JavaMethod(name,
                stripIndentation(code),
                stripIndentation(extractInsideCurlyBraces(code)),
                extractParams(methodDeclaration, javadoc),
                javaDocText));
    }

    private List<JavaMethodParam> extractParams(MethodDeclaration methodDeclaration, Javadoc javadoc) {
        List<String> paramNames = methodDeclaration.getParameters().stream().map(p -> p.getName().getIdentifier()).collect(toList());
        Map<String, String> javaDocTextByName = javadoc != null ?
                (javadoc.getBlockTags().stream().filter(b -> b.getType() == PARAM)
                        .collect(toMap(b -> b.getName().orElse(""), b -> b.getContent().toText()))) : Collections.emptyMap();

        return paramNames.stream().map(n -> new JavaMethodParam(n, javaDocTextByName.get(n))).collect(toList());
    }
}
