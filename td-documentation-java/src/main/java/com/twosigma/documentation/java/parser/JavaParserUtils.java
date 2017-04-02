package com.twosigma.documentation.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * @author mykola
 */
public class JavaParserUtils {
    public static String extractTopLevelJavaDoc(String classFileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(classFileContent);
        JavaDocVisitor javaDocVisitor = new JavaDocVisitor();
        compilationUnit.accept(javaDocVisitor, "test");

        return javaDocVisitor.getTopLevelJavaDoc();
    }

    public static String functionBody(String fileContent, String methodName) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaFuncBodyVisitor visitor = new JavaFuncBodyVisitor(fileContent);
        compilationUnit.accept(visitor, "test");

        return visitor.getDeclaration(methodName);
    }
}
