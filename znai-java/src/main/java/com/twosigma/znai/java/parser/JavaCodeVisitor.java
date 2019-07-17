package com.twosigma.znai.java.parser;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.github.javaparser.javadoc.description.JavadocSnippet;

import java.lang.reflect.Field;
import java.util.*;

import static com.github.javaparser.javadoc.JavadocBlockTag.Type.PARAM;
import static com.github.javaparser.javadoc.JavadocBlockTag.Type.RETURN;
import static com.twosigma.utils.StringUtils.*;
import static java.util.stream.Collectors.*;

public class JavaCodeVisitor extends VoidVisitorAdapter<String> {
    private final List<String> lines;
    private Map<String, JavaType> javaTypes;
    private List<JavaMethod> javaMethods;
    private List<EnumEntry> enumEntries;
    private Map<String, JavaField> javaFields;
    private String topLevelJavaDoc;
    private boolean isAfterInlinedTag;

    public JavaCodeVisitor(String code) {
        lines = Arrays.asList(code.split("\n"));
        javaTypes = new LinkedHashMap<>();
        javaMethods = new ArrayList<>();
        javaFields = new LinkedHashMap<>();
        enumEntries = new ArrayList<>();
    }

    public boolean hasType(String typeName) {
        return javaTypes.containsKey(typeName);
    }

    public JavaType findTypeDetails(String typeName) {
        if (! javaTypes.containsKey(typeName)) {
            throw new RuntimeException("no type found: " + typeName);
        }

        return javaTypes.get(typeName);
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

    public List<EnumEntry> getEnumEntries() {
        return enumEntries;
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
    public void visit(FieldDeclaration fieldDeclaration, String arg) {
        String javaDocText = fieldDeclaration.hasJavaDocComment() ? fieldDeclaration.getJavadocComment().parse().toText() : "";

        fieldDeclaration.getVariables().stream().map(vd -> vd.getName().getIdentifier())
                .forEach(name -> javaFields.put(name, new JavaField(name, javaDocText)));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String arg) {
        extractTopLevelJavaDoc(classOrInterfaceDeclaration);
        registerType(classOrInterfaceDeclaration);

        super.visit(classOrInterfaceDeclaration, arg);
    }

    @Override
    public void visit(EnumDeclaration enumDeclaration, String arg) {
        extractTopLevelJavaDoc(enumDeclaration);
        registerType(enumDeclaration);

        List<EnumEntry> entries = enumDeclaration.getEntries().stream().map(e -> new EnumEntry(e.getName().getIdentifier(),
                extractJavaDocDescription(e.getJavadocComment()), extractIsDeprecated(e))).collect(toList());

        enumEntries.addAll(entries);
        super.visit(enumDeclaration, arg);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, String arg) {
        String name = methodDeclaration.getName().getIdentifier();
        String code = JavaCodeUtils.extractCode(lines, methodDeclaration);

        JavadocComment javaDocComment = methodDeclaration.getJavadocComment();
        Javadoc javaDoc = javaDocComment != null ? javaDocComment.parse() : null;

        String javaDocText = (javaDoc != null) ?
                extractJavaDocDescription(javaDoc.getDescription()) :
                "";

        javaMethods.add(new JavaMethod(name,
                stripIndentation(JavaCodeUtils.removeSemicolonAtEnd(code)),
                stripIndentation(JavaCodeUtils.removeSemicolonAtEnd(extractInsideCurlyBraces(code))),
                JavaCodeUtils.removeSemicolonAtEnd(JavaCodeUtils.extractSignature(code)),
                extractParams(methodDeclaration, javaDoc),
                extractReturn(methodDeclaration, javaDoc),
                javaDocText));
    }

    private boolean extractIsDeprecated(EnumConstantDeclaration e) {
        List<MarkerAnnotationExpr> annotationNodes = e.getNodesByType(MarkerAnnotationExpr.class);
        return annotationNodes.stream().anyMatch(an -> an.getName().getIdentifier().equals("Deprecated"));
    }

    private String renderAllMethods() {
        return "    " + javaMethods.stream().map(JavaMethod::getNameWithTypes).collect(joining("\n    "));
    }

    private String renderAllFields() {
        return "    " + javaFields.keySet().stream().collect(joining("\n    "));
    }

    private String extractJavaDocDescription(JavadocComment javadocComment) {
        if (javadocComment == null) {
            return "";
        }

        Javadoc javadoc = javadocComment.parse();
        JavadocDescription description = javadoc.getDescription();

        return description == null ? "" : extractJavaDocDescription(description);
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

    private void extractTopLevelJavaDoc(TypeDeclaration declaration) {
        if (topLevelJavaDoc == null) {
            JavadocComment javadocComment = declaration.getJavadocComment();
            topLevelJavaDoc = (javadocComment != null) ?
                    extractJavaDocDescription(javadocComment.parse().getDescription()):
                    "";
        }
    }

    private void registerType(TypeDeclaration declaration) {
        String name = declaration.getName().getIdentifier();
        String code = JavaCodeUtils.extractCode(lines, declaration);

        JavaType javaType = new JavaType(name,
                stripIndentation(code),
                stripIndentation(extractInsideCurlyBraces(code)));
        javaTypes.put(name, javaType);
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

    private JavaMethodReturn extractReturn(MethodDeclaration methodDeclaration, Javadoc javadoc) {
        if (javadoc == null) {
            return null;
        }

        Optional<JavadocBlockTag> returnBlock = javadoc.getBlockTags().stream().filter(b -> b.getType() == RETURN).findFirst();
        if (! returnBlock.isPresent()) {
            return null;
        }

        return new JavaMethodReturn(methodDeclaration.getType().toString(),
                returnBlock.map(b -> extractJavaDocDescription(b.getContent())).orElse(""));
    }

    private static String eraseGenericType(String type) {
        return removeContentInsideBracketsInclusive(type);
    }
}
