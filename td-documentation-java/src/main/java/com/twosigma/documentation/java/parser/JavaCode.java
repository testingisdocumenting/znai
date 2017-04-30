package com.twosigma.documentation.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.twosigma.documentation.core.ComponentsRegistry;

/**
 * @author mykola
 */
public class JavaCode {
    private final JavaCodeVisitor codeVisitor;
    private String fileContent;
    private ComponentsRegistry componentsRegistry;

    public JavaCode(ComponentsRegistry componentsRegistry, String fileContent) {
        this.componentsRegistry = componentsRegistry;
        this.fileContent = fileContent;
        codeVisitor = parse(fileContent);
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getClassJavaDocText() {
        return codeVisitor.getTopLevelJavaDoc();
    }

    public String methodBody(String methodName) {
        return codeVisitor.getDetails(methodName).getFullBody();
    }

    public String methodBodyOnly(String methodName) {
        return codeVisitor.getDetails(methodName).getBodyOnly();
    }

    public String methodJavaDocText(String methodName) {
        return codeVisitor.getDetails(methodName).getJavaDocText();
    }

    private static JavaCodeVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaCodeVisitor visitor = new JavaCodeVisitor(fileContent);
        compilationUnit.accept(visitor, "JavaCode");
        return visitor;
    }
}
