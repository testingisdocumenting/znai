package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.github.javaparser.javadoc.description.JavadocSnippet;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.javaparser.javadoc.JavadocBlockTag.Type.PARAM;
import static com.twosigma.utils.StringUtils.extractInsideCurlyBraces;
import static com.twosigma.utils.StringUtils.removeContentInsideBracketsInclusive;
import static com.twosigma.utils.StringUtils.stripIndentation;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author mykola
 */
public class JavaCodeVisitor extends VoidVisitorAdapter<String> {
    private final List<String> lines;
    private List<JavaMethod> javaMethods;
    private Map<String, JavaField> javaFields;
    private String topLevelJavaDoc;
    private boolean isAfterInlinedTag;

    public JavaCodeVisitor(String code) {
        lines = Arrays.asList(code.split("\n"));
        javaMethods = new ArrayList<>();
        javaFields = new LinkedHashMap<>();
    }

    public JavaMethod findMethodDetails(String methodNameWithOptionalTypes) {
        String nameWithoutSpaces = methodNameWithOptionalTypes.replaceAll("\\s+", "");
        return javaMethods.stream().filter(
                m -> m.getName().equals(methodNameWithOptionalTypes) || m.getNameWithTypes().equals(nameWithoutSpaces))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no method found: " + methodNameWithOptionalTypes + "." +
                        "\nAvailable methods:\n" + renderAllMethods()));
    }

    public JavaMethod findMethodDetails(String methodName, List<String> paramNames) {
        return javaMethods.stream().filter(m -> m.getName().equals(methodName) && m.getParamNames().equals(paramNames))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no method found: " + methodName + " with params: " + paramNames));
    }

    public JavaField findFieldDetails(String fieldName) {
        if (! javaFields.containsKey(fieldName)) {
            throw new RuntimeException("no field found: " + fieldName);
        }

        return javaFields.get(fieldName);
    }

    public String findJavaDoc(String entryName) {
        if (javaFields.containsKey(entryName)) {
            return javaFields.get(entryName).getJavaDocText();
        }

        if (!hasMethodDetails(entryName)) {
            throw new RuntimeException("can't find method or field: " + entryName + "." +
                    "\nAvailable methods:\n" + renderAllMethods() +
            "\nAvailable fields:\n" + renderAllFields());
        }

        return findMethodDetails(entryName).getJavaDocText();
    }

    public String getTopLevelJavaDoc() {
        return topLevelJavaDoc;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, String arg) {
        JavadocComment javadocComment = n.getJavadocComment();

        topLevelJavaDoc = (javadocComment != null) ?
                extractJavaDocDescription(javadocComment.parse().getDescription()):
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

        JavadocComment javaDocComment = methodDeclaration.getJavadocComment();
        Javadoc javaDoc = javaDocComment != null ? javaDocComment.parse() : null;

        String javaDocText = (javaDoc != null) ?
                extractJavaDocDescription(javaDoc.getDescription()) :
                "";

        javaMethods.add(new JavaMethod(name,
                stripIndentation(removeSemicolonAtEnd(code)),
                stripIndentation(removeSemicolonAtEnd(extractInsideCurlyBraces(code))),
                removeSemicolonAtEnd(extractSignature(code)),
                extractParams(methodDeclaration, javaDoc),
                javaDocText));
    }

    private String renderAllMethods() {
        return "    " + javaMethods.stream().map(JavaMethod::getNameWithTypes).collect(joining("\n    "));
    }

    private String renderAllFields() {
        return "    " + javaFields.keySet().stream().collect(joining("\n    "));
    }

    private String removeSemicolonAtEnd(String code) {
        return code.endsWith(";") ? code.substring(0, code.length() - 1) : code;
    }

    private String extractSignature(String code) {
        int i = code.indexOf('{');
        return i == -1 ? code.trim() : code.substring(0, i).trim();
    }

    @SuppressWarnings("unchecked")
    private String extractJavaDocDescription(JavadocDescription description) {
        // TODO check if github java parser lib solved the problem with inlined tags UnsupportedOperation exception
        List<JavadocDescriptionElement> elements = getPrivateFieldValue(description,"elements");
        return elements.stream().map(this::elementToText).collect(joining(" "));
    }

    private String elementToText(JavadocDescriptionElement el) {
        if (el instanceof JavadocSnippet) {
            String result = isAfterInlinedTag ? el.toText().substring(1) : el.toText();
            isAfterInlinedTag = false;

            return result;
        }

        if (el instanceof JavadocInlineTag) {
            return "<code>" + extractTextFromInlinedTag(el).trim() + "</code>";
        }

        return el.toText();
    }

    private String extractTextFromInlinedTag(JavadocDescriptionElement el) {
        isAfterInlinedTag = true;
        return getPrivateFieldValue(el, "content");
    }

    @SuppressWarnings("unchecked")
    private <E> E getPrivateFieldValue(Object o, String fieldName) {
        try {
            Field elementsFields = o.getClass().getDeclaredField(fieldName);
            elementsFields.setAccessible(true);
            return (E) elementsFields.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(FieldDeclaration fieldDeclaration, String arg) {
        String javaDocText = fieldDeclaration.hasJavaDocComment() ? fieldDeclaration.getJavadocComment().parse().toText() : "";

        fieldDeclaration.getVariables().stream().map(vd -> vd.getName().getIdentifier())
                .forEach(name -> javaFields.put(name, new JavaField(name, javaDocText)));
    }

    private boolean hasMethodDetails(String methodNameWithOptionalTypes) {
        String nameWithoutSpaces = methodNameWithOptionalTypes.replaceAll("\\s+", "");

        return javaMethods.stream().anyMatch(m -> m.getName().equals(methodNameWithOptionalTypes) ||
                m.getNameWithTypes().equals(nameWithoutSpaces));
    }

    private List<JavaMethodParam> extractParams(MethodDeclaration methodDeclaration, Javadoc javadoc) {
        Map<String, String> typeByName = methodDeclaration.getParameters().stream()
                .collect(toMap(p -> p.getName().getIdentifier(), p -> eraseGenericType(p.getType().getElementType().toString())));

        List<String> paramNames = methodDeclaration.getParameters().stream().map(p -> p.getName().getIdentifier()).collect(toList());

        Map<String, String> javaDocTextByName = javadoc != null ?
                (javadoc.getBlockTags().stream().filter(b -> b.getType() == PARAM)
                        .collect(toMap(
                                b -> b.getName().orElse(""),
                                b -> extractJavaDocDescription(b.getContent())))) : Collections.emptyMap();

        return paramNames.stream().map(n -> new JavaMethodParam(n, javaDocTextByName.get(n), typeByName.get(n)))
                .collect(toList());
    }

    private static String eraseGenericType(String type) {
        return removeContentInsideBracketsInclusive(type);
    }
}
