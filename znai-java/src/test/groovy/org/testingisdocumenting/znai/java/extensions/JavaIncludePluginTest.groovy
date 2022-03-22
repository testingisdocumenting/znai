/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.java.extensions

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class JavaIncludePluginTest {
    @Test
    void "full source"() {
        def result = process("Simple.java", "")
        result.should == "class Simple {\n" +
                "    void methodA() {\n" +
                "        // inside method a\n" +
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
        def result = process("Simple.java", "{entry: ['methodA', 'createData'], signatureOnly: true}")
        result.should == "void methodA()\n" +
                "Data createData()"
    }

    @Test
    void "includes overloaded entry of java method"() {
        process("Simple.java", "{entry: 'methodB', signatureOnly: true}").should == "void methodB(String p)\n" +
                "void methodB(String p, Boolean b)"
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
    void "extract body only multiple entries"() {
        process("Simple.java", "{entry: ['methodA', 'createData'], bodyOnly: true}").should ==
                "// inside method a\n\n" +
                "return construction(a, b,\n" +
                "                    c, d);"
    }

    @Test
    void "extract multiple signatures of a method overloads"() {
        def result = process("Simple.java", "{entry: 'methodB', signatureOnly: true}")
        result.should == 'void methodB(String p)\n' +
                'void methodB(String p, Boolean b)'
    }

    @Test
    void "extract multiple full bodies of a method overloads"() {
        def result = process("Simple.java", "{entry: 'methodB'}")
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

    @Test
    void "validates highlight"() {
        code {
            process("Simple.java", "{highlight: -1}")
        } should throwException(IllegalArgumentException)
    }

    @Test
    void "provides auto title"() {
        processAndGetProps("Simple.java", "{autoTitle: true}").title.should == "Simple.java"
    }

    @Test
    void "exclude by regexp after body extract"() {
        process("WithExtraLines.java", "{entry: 'method', bodyOnly: true, excludeRegexp: 'doc.capture'}").should ==
                "// some text here\n" +
                "methodA();\n" +
                "// another text here"
    }

    @Test
    void "exclude by start-end after body extract"() {
        process("WithExtraLines.java",
                "{entry: 'method', bodyOnly: true, startLine: '//', endLine: '//', excludeStartEnd: true}").should ==
                "methodA();\n" +
                "doc.capture();"
    }

    private static Map<String, Object> processAndGetProps(String fileName, String params) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-java: $fileName $params")
    }

    private static String process(String fileName, String params) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-java: $fileName $params")
    }
}
