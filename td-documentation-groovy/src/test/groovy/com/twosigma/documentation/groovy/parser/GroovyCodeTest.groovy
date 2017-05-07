package com.twosigma.documentation.groovy.parser

import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class GroovyCodeTest {
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
    void "sample method"(def test) {
        statement1()
        statement2()

        if (logic) {
            doAction()
        }
    }
}"""

    GroovyCode groovyCode = new GroovyCode(null, Paths.get(""), code)

    @Test
    void "extracts method body"() {
        String body = groovyCode.methodBody("sample method")

        Assert.assertEquals("void \"sample method\"(def test) {\n" +
                "    statement1()\n" +
                "    statement2()\n" +
                "\n" +
                "    if (logic) {\n" +
                "        doAction()\n" +
                "    }\n" +
                "}", body);
    }


    @Test
    void "extracts method body only"() {
        String body = groovyCode.methodBodyOnly("sample method");

        Assert.assertEquals("statement1()\n" +
                "statement2()\n" +
                "\n" +
                "if (logic) {\n" +
                "    doAction()\n" +
                "}", body);
    }
}
