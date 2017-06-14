package com.twosigma.documentation.groovy.parser

import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.testing.Ddjt.code
import static com.twosigma.testing.Ddjt.throwException

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
    void "sample method"(List<String> test, def b, Map<Integer, String> c) {
        statement1()
        statement2()

        if (logic) {
            doAction()
        }
    }

    void "another method"(Map<Integer, String> c, Boolean flag) {
        doAnotherAction()
    }

    void "another method"(Map<Integer, String> c) {
        doAnotherActionWithoutFlag()
    }
    
    void noParameters() {
    //nothing
    }
}"""

    GroovyCode groovyCode = new GroovyCode(null, Paths.get(""), code)

    @Test
    void "find method by name"() {
        def method = groovyCode.findMethod("sample method")

        Assert.assertEquals("void \"sample method\"(List<String> test, def b, Map<Integer, String> c) {\n" +
                "    statement1()\n" +
                "    statement2()\n" +
                "\n" +
                "    if (logic) {\n" +
                "        doAction()\n" +
                "    }\n" +
                "}", method.fullBody)

        Assert.assertEquals("statement1()\n" +
                "statement2()\n" +
                "\n" +
                "if (logic) {\n" +
                "    doAction()\n" +
                "}", method.bodyOnly)
    }

    @Test
    void "find method by name and parameters"() {
        def method = groovyCode.findMethod("another method(Map, Boolean)")
        Assert.assertEquals("doAnotherAction()", method.bodyOnly)

        def noParamsMethod = groovyCode.findMethod("noParameters")
        Assert.assertEquals("//nothing", noParamsMethod.bodyOnly)
    }

    @Test
    void "list all available methods with signatures when no match is found"() {
        code {
            groovyCode.findMethod("non existing")
        } should throwException("no method found: non existing.\n" +
                "Available methods:\n" +
                "    sample method(List,def,Map)\n" +
                "    another method(Map,Boolean)\n" +
                "    another method(Map)\n" +
                "    noParameters()")
    }
}
