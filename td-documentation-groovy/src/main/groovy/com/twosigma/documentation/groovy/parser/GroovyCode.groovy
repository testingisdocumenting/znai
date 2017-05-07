package com.twosigma.documentation.groovy.parser

import com.twosigma.documentation.core.ComponentsRegistry
import groovyjarjarantlr.collections.AST
import org.codehaus.groovy.antlr.GroovySourceAST
import org.codehaus.groovy.antlr.SourceBuffer
import org.codehaus.groovy.antlr.UnicodeEscapingReader
import org.codehaus.groovy.antlr.parser.GroovyLexer
import org.codehaus.groovy.antlr.parser.GroovyRecognizer
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal
import org.codehaus.groovy.antlr.treewalker.SourcePrinter
import org.codehaus.groovy.antlr.treewalker.Visitor
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter
import org.codehaus.groovy.tools.groovydoc.SimpleGroovyClassDocAssembler

import java.nio.charset.StandardCharsets
import java.nio.file.Path

/**
 * @author mykola
 */
class GroovyCode {
    private ComponentsRegistry componentsRegistry
    private Path filePath

    String fileContent
    GroovyCodeVisitor codeVisitor

    GroovyCode(ComponentsRegistry componentsRegistry, Path filePath, String fileContent) {
        this.fileContent = fileContent
        this.filePath = filePath
        this.componentsRegistry = componentsRegistry

        this.codeVisitor = parse(fileContent)
    }

    String methodBody(String methodName) {
        return codeVisitor.getDetails(methodName).getFullBody();
    }

    String methodBodyOnly(String methodName) {
        return codeVisitor.getDetails(methodName).getBodyOnly();
    }

    private static GroovyCodeVisitor parse(String code) {
        Reader reader = new StringReader(code)

        GroovyLexer lexer = new GroovyLexer(reader)
        SourceBuffer sourceBuffer = new SourceBuffer()

        GroovyRecognizer parser = GroovyRecognizer.make(lexer)
        parser.setSourceBuffer(sourceBuffer)
        parser.compilationUnit()
        AST ast = parser.getAST()

        def visitor = new GroovyCodeVisitor(code)
        def traversal = new SourceCodeTraversal(visitor)
        traversal.process(ast)

        return visitor
    }
}
