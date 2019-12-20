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

package com.twosigma.znai.java.extensions

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.webtau.Matchers.code
import static com.twosigma.webtau.Matchers.throwException

class JavaIncludePluginTest {
    @Test
    void "full source"() {
        def result = process("Simple.java", "")
        result.should == "class Simple {\n" +
                "    void methodA() {\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    void methodB(String p) {\n" +
                "        doB();\n" +
                "    }\n" +
                "\n" +
                "    void methodB(String p, Boolean b) {\n" +
                "        doBPlus();\n" +
                "    }\n" +
                "\n" +
                "    Data createData() {\n" +
                "        return construction(a, b,\n" +
                "                            c, d);\n" +
                "    }\n" +
                "}"
    }

    @Test
    void "includes multiple entries of java method signatures"() {
        def result = process("Simple.java", "{entries: ['methodA', 'createData'], signatureOnly: true}")
        result.should == "void methodA()\n" +
                "Data createData()"
    }

    @Test
    void "includes overloaded entry of java method"() {
        process("Simple.java", "{entry: 'methodB', signatureOnly: true}").should == "void methodB(String p)"
        process("Simple.java", "{entry: 'methodB(String, Boolean)', signatureOnly: true}").should == "void methodB(String p, Boolean b)"

        code {
            process("Simple.java", "{entry: 'methodB(String2)', signatureOnly: true}")
        } should throwException(~/no method found: methodB/)
    }

    @Test
    void "extract body only"() {
        process("Simple.java", "{entry: 'createData', bodyOnly: true}").should ==
                "return construction(a, b,\n" +
                "                    c, d);"
    }

    @Test
    void "extract multiple signatures of a method overloads"() {
        def result = process("Simple.java", "{entries: 'methodB', signatureOnly: true}")
        result.should == 'void methodB(String p)\n' +
                'void methodB(String p, Boolean b)'
    }

    @Test
    void "extract multiple full bodies of a method overloads"() {
        def result = process("Simple.java", "{entries: 'methodB'}")
        result.should == 'void methodB(String p) {\n' +
                '    doB();\n' +
                '}\n' +
                '\n' +
                'void methodB(String p, Boolean b) {\n' +
                '    doBPlus();\n' +
                '}'
    }

    @Test
    void "optionally removes return in body only mode"() {
        process("Simple.java", "{entry: 'createData', bodyOnly: true, removeReturn: true}").should ==
                "construction(a, b,\n" +
                "             c, d);"
    }

    @Test
    void "optionally removes return and semicolon in body only mode"() {
        process("Simple.java", "{entry: 'createData', bodyOnly: true, " +
                "removeReturn: true, removeSemicolon: true}").should ==
                "construction(a, b,\n" +
                "             c, d)"

    }

    private static String process(String fileName, String params) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-java: $fileName $params")
    }
}
