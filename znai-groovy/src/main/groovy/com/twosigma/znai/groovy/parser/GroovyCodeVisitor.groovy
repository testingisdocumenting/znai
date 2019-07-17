package com.twosigma.znai.groovy.parser

import org.codehaus.groovy.antlr.GroovySourceAST
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter

import java.util.stream.Collectors

import static com.twosigma.utils.StringUtils.extractInsideCurlyBraces
import static com.twosigma.utils.StringUtils.removeContentInsideBracketsInclusive
import static com.twosigma.utils.StringUtils.stripIndentation
import static org.codehaus.groovy.antlr.parser.GroovyTokenTypes.*

class GroovyCodeVisitor extends VisitorAdapter {
    private final List<String> lines
    private List<GroovyMethod> methods
    private List<GroovyType> types

    GroovyCodeVisitor(String code) {
        lines = Arrays.asList(code.split("\n"))
        methods = []
        types = []
    }

    GroovyMethod findMethodDetails(String methodNameWithOptionalTypes) {
        def method = methods.find { it.matchesNameAndType(methodNameWithOptionalTypes) }
        if (! method) {
            throw new RuntimeException("no method found: " + methodNameWithOptionalTypes + ".\n" +
                    "Available methods:\n" + renderAvailableEntries(methods, { it.nameWithTypes }))
        }

        return method
    }

    boolean hasTypeDetails(String typeName) {
        return types.any { it.name == typeName}
    }

    GroovyType findTypeDetails(String typeName) {
        def type = types.find { it.name == typeName }
        if (! type) {
            throw new RuntimeException("no type found: " + typeName + ".\n" +
                "Available types:\n" + renderAvailableEntries(types, { it.name }))
        }

        return type
    }

    private String renderAvailableMethods() {
        return "    " + methods.collect { it.nameWithTypes }.join("\n    ")
    }

    @Override
    void visitMethodDef(GroovySourceAST t, int visit) {
        if (visit != OPENING_VISIT) {
            return
        }

        def methodName = t.childOfType(IDENT).getText()
        def types = extractParameterTypes(t)

        String code = lines.subList(t.getLine() - 1, t.getLineLast()).stream().collect(Collectors.joining("\n"))
        String fullBody = stripIndentation(code)
        String bodyOnly = stripIndentation(extractInsideCurlyBraces(code))

        methods.add(new GroovyMethod(name: methodName,
                nameWithTypes: methodName + "(" + types.join(",") + ")",
                fullBody: fullBody, bodyOnly: bodyOnly))
    }

    @Override
    void visitClassDef(GroovySourceAST t, int visit) {
        def className = t.childOfType(IDENT).getText()
        def extractedCode = extractCode(t.getLine(), t.getLineLast())

        types.add(new GroovyType(className, extractedCode.full, extractedCode.bodyOnly))

        super.visitClassDef(t, visit)
    }

    private ExtractedCode extractCode(int startLine, int endLine) {
        String code = lines.subList(startLine - 1, endLine).stream().collect(Collectors.joining("\n"))
        return new ExtractedCode(full: stripIndentation(code),
                bodyOnly: stripIndentation(extractInsideCurlyBraces(code)))
    }

    private List<String> extractParameterTypes(GroovySourceAST t) {
        def result = []
        def paramsNode = t.childOfType(PARAMETERS).getFirstChild()

        while (paramsNode) {
            def typeNode = paramsNode.childOfType(TYPE)

            if (typeNode) {
                def line = lines[typeNode.getLine() - 1]
                result << extractTypeOnly(line.substring(typeNode.getColumn() - 1, typeNode.getColumnLast() - 1))
            } else {
                result << "def"
            }

            paramsNode = paramsNode.nextSibling
        }

        return result
    }

    private static String extractTypeOnly(String typeAndName) {
        def lastSpaceIdx = typeAndName.lastIndexOf(' ')
        def type = eraseGeneric(lastSpaceIdx == -1 ? typeAndName : typeAndName.substring(0, lastSpaceIdx))

        return type.trim()
    }

    private static String eraseGeneric(String type) {
        return removeContentInsideBracketsInclusive(type)
    }

    private static String renderAvailableEntries(list, getter) {
        return "    " + list.collect(getter).join("\n    ")
    }

    private static class ExtractedCode {
        String full
        String bodyOnly
    }
}
