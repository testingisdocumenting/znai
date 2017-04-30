package com.twosigma.documentation.java.parser

import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class JavaParserUtilsTest {
    String code = """
/**
 * this is a top level java doc
 *
 * @see other
 * @author ignore
 */
class HelloWorld {
    /**
     * method level java doc 
     * @param test test param 
     */
    public void sampleMethod(String test) {
        statement1();
        statement2();

        if (logic) {
            doAction();
        }
    }
}"""

    @Test
    void "extracts method body"() {
        String body = JavaParserUtils.methodBody(code, "sampleMethod");

        Assert.assertEquals("public void sampleMethod(String test) {\n" +
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

    @Test
    void "extracts top level java doc"() {
        Assert.assertEquals("this is a top level java doc", JavaParserUtils.classJavaDocText(code))
    }

    @Test
    void "extracts method java doc"() {
        Assert.assertEquals("method level java doc", JavaParserUtils.methodJavaDocText(code, "sampleMethod"))
    }
}
