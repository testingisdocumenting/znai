package com.twosigma.documentation.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * @author mykola
 */
public class JavaParserUtils {
    public static String classJavaDocText(String fileContent) {
        return parse(fileContent).getTopLevelJavaDoc();
    }

    public static String methodBody(String fileContent, String methodName) {
        return parse(fileContent).getDetails(methodName).getFullBody();
    }

    public static String methodBodyOnly(String fileContent, String methodName) {
        return parse(fileContent).getDetails(methodName).getBodyOnly();
    }

    public static String methodJavaDocText(String fileContent, String methodName) {
        return parse(fileContent).getDetails(methodName).getJavaDocText();
    }

    private static JavaCodeVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaCodeVisitor visitor = new JavaCodeVisitor(fileContent);
        compilationUnit.accept(visitor, "JavaParserUtils");
        return visitor;
    }
}
