/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.groovy.parser

import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

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

    @Test
    void "knows if type with a given name is present"() {
        groovyCode.hasTypeDetails("HelloWorld").should == true
        groovyCode.hasTypeDetails("Unknown").should == false
    }

    @Test
    void "find type details by name"() {
        def type = groovyCode.findType("HelloWorld")
        Assert.assertEquals("class HelloWorld {\n" +
                "    /**\n" +
                "     * method level java doc \n" +
                "     * @param test test param \n" +
                "     */\n" +
                "    void \"sample method\"(List<String> test, def b, Map<Integer, String> c) {\n" +
                "        statement1()\n" +
                "        statement2()\n" +
                "\n" +
                "        if (logic) {\n" +
                "            doAction()\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void \"another method\"(Map<Integer, String> c, Boolean flag) {\n" +
                "        doAnotherAction()\n" +
                "    }\n" +
                "\n" +
                "    void \"another method\"(Map<Integer, String> c) {\n" +
                "        doAnotherActionWithoutFlag()\n" +
                "    }\n" +
                "    \n" +
                "    void noParameters() {\n" +
                "    //nothing\n" +
                "    }\n" +
                "}", type.fullBody)

        Assert.assertEquals("/**\n" +
                " * method level java doc \n" +
                " * @param test test param \n" +
                " */\n" +
                "void \"sample method\"(List<String> test, def b, Map<Integer, String> c) {\n" +
                "    statement1()\n" +
                "    statement2()\n" +
                "\n" +
                "    if (logic) {\n" +
                "        doAction()\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "void \"another method\"(Map<Integer, String> c, Boolean flag) {\n" +
                "    doAnotherAction()\n" +
                "}\n" +
                "\n" +
                "void \"another method\"(Map<Integer, String> c) {\n" +
                "    doAnotherActionWithoutFlag()\n" +
                "}\n" +
                "    \n" +
                "void noParameters() {\n" +
                "//nothing\n" +
                "}", type.bodyOnly)

    }
}
