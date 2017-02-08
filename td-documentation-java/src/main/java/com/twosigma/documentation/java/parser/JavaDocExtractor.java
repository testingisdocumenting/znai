package com.twosigma.documentation.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * @author mykola
 */
public class JavaDocExtractor {
    public static String extractTopLevel(String classFileContent) {
        CompilationUnit compilationUnit = JavaParser.parse(classFileContent);
        JavaDocVisitor javaDocVisitor = new JavaDocVisitor();
        compilationUnit.accept(javaDocVisitor, "test");

        return javaDocVisitor.getTopLevelJavaDoc();
    }

    public static void main(String[] args) {
        CompilationUnit compilationUnit = JavaParser.parse("/** test doc \n" +
                " * test paragraph 1\n" +
                " * test paragraph 2\n" +
                " *\n" +
                "  * @author mykola\n" +
                " */\n" +
                "public class JavaDocExtractor {\n" +
                "    public static void main(String[] args) {\n" +
                "        JavaParser.parse();\n" +
                "    }\n" +
                "}\n");

        JavaDocVisitor javaDocVisitor = new JavaDocVisitor();
        compilationUnit.accept(javaDocVisitor, "test");

        System.out.println("-----");
        System.out.println(javaDocVisitor.getTopLevelJavaDoc());
    }
}
