package com.twosigma.documentation.java.parser

import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class JavaParserUtilsTest {
    @Test
    void "extracts function body"() {
        String body = JavaParserUtils.functionBody("package com.twosigma.documentation.java.parser;\n" +
                "\n" +
                "\n" +
                "public class JavaDocVisitor extends VoidVisitorAdapter {\n" +
                "    private String topLevelJavaDoc;\n" +
                "\n" +
                "    public String getTopLevelJavaDoc() {\n" +
                "        return topLevelJavaDoc;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void visit(ClassOrInterfaceDeclaration n, Object arg) {\n" +
                "        JavadocComment javadocComment = n.getJavadocComment();\n" +
                "\n" +
                "        Javadoc javadoc = javadocComment.parse();\n" +
                "        topLevelJavaDoc = javadoc.getDescription().toText();\n" +
                "\n" +
                "        super.visit(n, arg);\n" +
                "    }\n" +
                "}\n", "visit");

        Assert.assertEquals("JavadocComment javadocComment = n.getJavadocComment();\n" +
                "\n" +
                "Javadoc javadoc = javadocComment.parse();\n" +
                "topLevelJavaDoc = javadoc.getDescription().toText();\n" +
                "\n" +
                "super.visit(n, arg);", body);

    }
}
