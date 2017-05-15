package com.twosigma.documentation.java.parser

import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class JavaCodeTest {
    String code = """
/**
 * this is a <b>top</b> level java doc
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

    /**
     * method level java doc 
     * @param test test param
     * @param name name of the param 
     */
    public void sampleMethod(String test, String name) {
        statement3();
        statement4();
    }
}"""

    JavaCode javaCode = new JavaCode(code)

    @Test
    void "extracts method by name"() {
        def method = javaCode.methodByName("sampleMethod")

        Assert.assertEquals("public void sampleMethod(String test) {\n" +
                "    statement1();\n" +
                "    statement2();\n" +
                "\n" +
                "    if (logic) {\n" +
                "        doAction();\n" +
                "    }\n" +
                "}", method.fullBody)

        Assert.assertEquals("statement1();\n" +
                "statement2();\n" +
                "\n" +
                "if (logic) {\n" +
                "    doAction();\n" +
                "}", method.bodyOnly)

        def params = method.params.collect { [it.name, it.javaDocText] }
        assert params == [["test", "test param"]]

        Assert.assertEquals("method level java doc", method.getJavaDocText())
    }

    @Test
    void "extracts method body by params"() {
        def method = javaCode.methodByNameAndParams("sampleMethod", ["test", "name"])

        def params = method.params.collect { [it.name, it.javaDocText] }
        assert params == [["test", "test param"],
                          ["name", "name of the param"]]

        Assert.assertEquals("public void sampleMethod(String test, String name) {\n" +
                "    statement3();\n" +
                "    statement4();\n" +
                "}", method.fullBody)
    }

    @Test
    void "extracts top level java doc"() {
        Assert.assertEquals("this is a <b>top</b> level java doc", javaCode.getClassJavaDocText())
    }
}
