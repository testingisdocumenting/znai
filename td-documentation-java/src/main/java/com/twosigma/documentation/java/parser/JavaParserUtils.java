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

    public static String methodBody(String fileContent, String methodName) {
        return parse(fileContent).getDeclaration(methodName);
    }

    public static String methodBodyOnly(String fileContent, String methodName) {
        return parse(fileContent).getDeclarationBodyOnly(methodName);
    }

    private static JavaMethodBodyVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaMethodBodyVisitor visitor = new JavaMethodBodyVisitor(fileContent);
        compilationUnit.accept(visitor, "test");
        return visitor;
    }
}
