package com.example

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * @author mykola
 */
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class Ast extends ClassCodeExpressionTransformer implements ASTTransformation {
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {

    }

    @Override
    protected SourceUnit getSourceUnit() {
        return null
    }
}
