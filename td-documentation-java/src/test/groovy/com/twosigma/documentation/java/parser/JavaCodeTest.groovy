package com.twosigma.documentation.java.parser

import org.junit.Assert
import org.junit.Test

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
     * overloaded method java doc 
     * @param test test param
     * @param name name of the param 
     * @return list of samples
     */
    public List<String> sampleMethod(String test, List<String> name) {
        statement3();
        statement4();
    }
    
    public String noReturnTag() {
        return "";
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
        def method = javaCode.findMethod("sampleMethod")

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
    void "extracts method body by params types"() {
        def method = javaCode.findMethod("sampleMethod(  String , List )")

        def params = method.params.collect { [it.name, it.javaDocText, it.type] }
        assert params == [["test", "test param", "String"],
                          ["name", "name of the param", "List"]]

        Assert.assertEquals("public List<String> sampleMethod(String test, List<String> name) {\n" +
                "    statement3();\n" +
                "    statement4();\n" +
                "}", method.fullBody)
    }

    @Test
    void "extracts return information"() {
        def method = javaCode.findMethod("sampleMethod(String,List)")

        method.javaMethodReturn.type.should == 'List<String>'
        method.javaMethodReturn.javaDocText.should == 'list of samples'
    }

    @Test
    void "return information is null when type is void"() {
        def method = javaCode.findMethod("sampleMethod(String)")
        assert method.javaMethodReturn == null
    }

    @Test
    void "return information is null when return tag is missing"() {
        def method = javaCode.findMethod("noReturnTag")
        assert method.javaMethodReturn == null
    }

    @Test
    void "extracts method from interface"() {
        def method = javaCodeInterface.findMethod("sampleMethod")

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
    void "extracts java doc by name with optional signature"() {
        Assert.assertEquals("Each year we hire students from different universities to increase\ndiversity\n",
                javaCode.findJavaDoc("numberOfStudents"))

        Assert.assertEquals("method level java doc  <code>package.Class</code> ",
                javaCode.findJavaDoc("sampleMethod"))

        Assert.assertEquals("overloaded method java doc",
                javaCode.findJavaDoc("sampleMethod(String,List)"))
    }

    @Test
    void "list all fields and methods when extract java doc by non existing method or field"() {
        code {
            javaCode.findJavaDoc("nonExisting")
        } should throwException("can't find method or field: nonExisting.\n" +
                "Available methods:\n" +
                "    sampleMethod(String)\n" +
                "    sampleMethod(String,List)\n" +
                "    noReturnTag()\n" +
                "Available fields:\n" +
                "    numberOfStudents\n" +
                "    fieldWithNoComment")
    }

    @Test
    void "list all available methods with signatures when no match is found"() {
        code {
            javaCode.findMethod("nonExisting")
        } should throwException("no method found: nonExisting.\n" +
                "Available methods:\n" +
                "    sampleMethod(String)\n" +
                "    sampleMethod(String,List)\n" +
                "    noReturnTag()")
    }

    @Test
    void "extracts java doc by entry name from interface"() {
        Assert.assertEquals("method level java doc  <code>package.Class</code> ",
                javaCode.findJavaDoc("sampleMethod"))
    }

    @Test
    void "extracts top level java doc"() {
        Assert.assertEquals("this is a <b>top</b> level java doc", javaCode.getClassJavaDocText())
    }
}
