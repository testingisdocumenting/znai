package com.twosigma.documentation.java.parser

import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.testing.Ddjt.code
import static com.twosigma.testing.Ddjt.throwException

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
     * Each year we hire students from different universities to increase
     * diversity
     */
    private int numberOfStudents;
    
    private boolean fieldWithNoComment;

    /**
     * method level java doc {@link package.Class}
     * @param test test param {@link package.Param}
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

    String interfaceCode = """
/**
 * this is a <b>top</b> level java doc
 *
 * @see other
 * @author ignore
 */
interface HelloWorld {
    /**
     * method level java doc {@link package.Class}
     * @param test test param 
     */
    public void sampleMethod(String test);

     /**
     * method level java doc overloaded
     * @param test test param 
     */
     public void sampleMethod(String test, String name);
}"""

    JavaCode javaCode = new JavaCode(code)
    JavaCode javaCodeInterface = new JavaCode(interfaceCode)

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

        Assert.assertEquals("public void sampleMethod(String test)", method.signatureOnly)

        def params = method.params.collect { [it.name, it.javaDocText] }
        assert params == [["test", "test param  <code>package.Param</code> "]]

        Assert.assertEquals("method level java doc  <code>package.Class</code> ", method.getJavaDocText())
    }

    @Test
    void "extracts method body by params"() {
        def method = javaCode.methodByNameAndParams("sampleMethod", ["test", "name"])

        def params = method.params.collect { [it.name, it.javaDocText, it.type] }
        assert params == [["test", "test param", "String"],
                          ["name", "name of the param", "String"]]

        Assert.assertEquals("public void sampleMethod(String test, String name) {\n" +
                "    statement3();\n" +
                "    statement4();\n" +
                "}", method.fullBody)
    }

    @Test
    void "extracts method from interface"() {
        def method = javaCodeInterface.methodByName("sampleMethod")

        Assert.assertEquals("public void sampleMethod(String test)", method.fullBody)
        Assert.assertEquals("", method.bodyOnly)
        Assert.assertEquals("public void sampleMethod(String test)", method.signatureOnly)

        def params = method.params.collect { [it.name, it.javaDocText] }
        assert params == [["test", "test param"]]

        Assert.assertEquals("method level java doc  <code>package.Class</code> ", method.getJavaDocText())
    }

    @Test
    void "extracts field level java doc"() {
        Assert.assertEquals("Each year we hire students from different universities to increase\ndiversity\n",
                javaCode.fieldByName("numberOfStudents").getJavaDocText())

        Assert.assertEquals("",
                javaCode.fieldByName("fieldWithNoComment").getJavaDocText())
    }

    @Test
    void "extracts java doc by entry name"() {
        Assert.assertEquals("Each year we hire students from different universities to increase\ndiversity\n",
                javaCode.findJavaDocByName("numberOfStudents"))

        Assert.assertEquals("method level java doc  <code>package.Class</code> ",
                javaCode.findJavaDocByName("sampleMethod"))

        code {
            javaCode.findJavaDocByName("nonExisting")
        } should throwException("can't find method or field with name: nonExisting")
    }

    @Test
    void "extracts java doc by entry name from interface"() {
        Assert.assertEquals("method level java doc  <code>package.Class</code> ",
                javaCode.findJavaDocByName("sampleMethod"))
    }

    @Test
    void "extracts top level java doc"() {
        Assert.assertEquals("this is a <b>top</b> level java doc", javaCode.getClassJavaDocText())
    }
}
