package com.twosigma.documentation.java.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;

/**
 * @author mykola
 */
public class JavaDocVisitor extends VoidVisitorAdapter {
    private String topLevelJavaDoc;

    public String getTopLevelJavaDoc() {
        return topLevelJavaDoc;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        JavadocComment javadocComment = n.getJavadocComment();

        Javadoc javadoc = javadocComment.parse();
        topLevelJavaDoc = javadoc.getDescription().toText();
    }
}
