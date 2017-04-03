package com.twosigma.documentation.java.parser

import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class JavaParserUtilsTest {
    String code = "class HelloWorld {\n" +
            "    public void sampleMethod() {\n" +
            "        statement1();\n" +
            "        statement2();\n" +
            "\n" +
            "        if (logic) {\n" +
            "            doAction();\n" +
            "        }\n" +
            "    }\n" +
            "}"

    @Test
    void "extracts method body"() {
        String body = JavaParserUtils.methodBody(code, "sampleMethod");

        Assert.assertEquals("public void sampleMethod() {\n" +
                "    statement1();\n" +
                "    statement2();\n" +
                "\n" +
                "    if (logic) {\n" +
                "        doAction();\n" +
                "    }\n" +
                "}", body);
    }

    @Test
    void "extracts method body only"() {
        String body = JavaParserUtils.methodBodyOnly(code, "sampleMethod");

        Assert.assertEquals("statement1();\n" +
                "statement2();\n" +
                "\n" +
                "if (logic) {\n" +
                "    doAction();\n" +
                "}", body);

    }
}
