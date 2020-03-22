/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.groovy.parser

import com.twosigma.znai.core.ComponentsRegistry
import groovyjarjarantlr.collections.AST
import org.codehaus.groovy.antlr.SourceBuffer
import org.codehaus.groovy.antlr.parser.GroovyLexer
import org.codehaus.groovy.antlr.parser.GroovyRecognizer
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal

import java.nio.file.Path

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

    GroovyMethod findMethod(String methodNameWithOptionalTypes) {
        return codeVisitor.findMethodDetails(methodNameWithOptionalTypes)
    }

    boolean hasTypeDetails(String typeName) {
        return codeVisitor.hasTypeDetails(typeName)
    }

    GroovyType findType(String typeName) {
        return codeVisitor.findTypeDetails(typeName)
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
