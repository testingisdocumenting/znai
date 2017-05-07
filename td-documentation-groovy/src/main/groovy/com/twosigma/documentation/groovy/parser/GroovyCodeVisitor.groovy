package com.twosigma.documentation.groovy.parser

import org.codehaus.groovy.antlr.GroovySourceAST
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter

import java.util.stream.Collectors

import static com.twosigma.utils.StringUtils.extractInsideCurlyBraces
import static com.twosigma.utils.StringUtils.stripIndentation

/**
 * @author mykola
 */
class GroovyCodeVisitor extends VisitorAdapter {
    private final List<String> lines
    private Map<String, GroovyMethodDetails> detailsByName

    GroovyCodeVisitor(String code) {
        lines = Arrays.asList(code.split("\n"))
        detailsByName = new HashMap<>()
    }

    GroovyMethodDetails getDetails(String methodName) {
        if (! detailsByName.containsKey(methodName)) {
            throw new RuntimeException("no method found: " + methodName);
        }

        return detailsByName.get(methodName)
    }

    @Override
    void visitMethodDef(GroovySourceAST t, int visit) {
        def methodName = t.childOfType(GroovyTokenTypes.IDENT).getText()

        String code = lines.subList(t.getLine() - 1, t.getLineLast()).stream().collect(Collectors.joining("\n"))

        String fullBody = stripIndentation(code)
        String bodyOnly = stripIndentation(extractInsideCurlyBraces(code))

        detailsByName.put(methodName, new GroovyMethodDetails(fullBody: fullBody, bodyOnly: bodyOnly))
    }
}
