package com.twosigma.documentation.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

/**
 * @author mykola
 */
public class JavaCode {
    private final JavaCodeVisitor codeVisitor;
    private String fileContent;

    public JavaCode(String fileContent) {
        this.fileContent = fileContent;
        codeVisitor = parse(fileContent);
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getClassJavaDocText() {
        return codeVisitor.getTopLevelJavaDoc();
    }

    public String findJavaDocByName(String entryName) {
        return codeVisitor.findJavaDocByName(entryName);
    }

    public JavaMethod methodByName(String methodName) {
        return codeVisitor.findMethodDetails(methodName);
    }

    public JavaMethod methodByNameAndParams(String methodName, List<String> paramNames) {
        return codeVisitor.findMethodDetails(methodName, paramNames);
    }

    public JavaField fieldByName(String fieldName) {
        return codeVisitor.findFieldDetails(fieldName);
    }

    private static JavaCodeVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaCodeVisitor visitor = new JavaCodeVisitor(fileContent);
        compilationUnit.accept(visitor, "JavaCode");
        return visitor;
    }
}
