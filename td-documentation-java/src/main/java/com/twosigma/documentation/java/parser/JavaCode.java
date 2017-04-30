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
    private Path filePath;
    private String fileContent;
    private ComponentsRegistry componentsRegistry;

    public JavaCode(ComponentsRegistry componentsRegistry, Path filePath, String fileContent) {
        this.componentsRegistry = componentsRegistry;
        this.filePath = filePath;
        this.fileContent = fileContent;
        codeVisitor = parse(fileContent);
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getClassJavaDocText() {
        return codeVisitor.getTopLevelJavaDoc();
    }

    public List<DocElement> getClassJavaDocAsDocElements() {
        return HtmlToDocElementConverter.convert(componentsRegistry, filePath, getClassJavaDocText());
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

    public List<DocElement> methodJavaDocTextAsDocElements(String methodName) {
        return HtmlToDocElementConverter.convert(componentsRegistry, filePath, methodJavaDocText(methodName));
    }

    private static JavaCodeVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(fileContent);
        JavaCodeVisitor visitor = new JavaCodeVisitor(fileContent);
        compilationUnit.accept(visitor, "JavaCode");
        return visitor;
    }
}
