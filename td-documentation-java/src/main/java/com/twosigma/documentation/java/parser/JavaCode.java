package com.twosigma.documentation.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.java.parser.html.HtmlToDocElementConverter;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
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

    public JavaMethod methodByName(String methodName) {
        return codeVisitor.getDetails(methodName);
    }

    public JavaMethod methodByNameAndParams(String methodName, List<String> paramNames) {
        return codeVisitor.getDetails(methodName, paramNames);
    }

    private static JavaCodeVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaCodeVisitor visitor = new JavaCodeVisitor(fileContent);
        compilationUnit.accept(visitor, "JavaCode");
        return visitor;
    }
}
