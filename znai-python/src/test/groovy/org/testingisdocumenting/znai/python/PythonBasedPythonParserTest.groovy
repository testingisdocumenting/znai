/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.python

import org.junit.Test

import java.nio.file.Paths

class PythonBasedPythonParserTest {
    @Test
    void "parsing python using python process"() {
        def parsed = PythonBasedPythonParser.INSTANCE.parse(Paths.get("src/test/resources/example.py"))
        parsed.findEntryByName("func_no_docs").should == [
                name: "func_no_docs",
                type: "function",
                content: "def func_no_docs():\n" +
                        "    a = 2\n" +
                        "    b = 3\n" +
                        "    return a + b",
                bodyOnly: "    a = 2\n" +
                        "    b = 3\n" +
                        "    return a + b",
                docString: ""
        ]

        parsed.findEntryByName("my_func").should == [
                name: "my_func",
                type: "function",
                content: "def my_func(a, b,\n" +
                        "            c, d):\n" +
                        "    \"\"\"\n" +
                        "    text inside my *func* doc\n" +
                        "\n" +
                        "    Parameters\n" +
                        "    ----------\n" +
                        "    a: String\n" +
                        "      description\n" +
                        "    \"\"\"\n" +
                        "\n" +
                        "    e = a + b\n" +
                        "    f = c + d\n" +
                        "\n" +
                        "    return e + f",
                bodyOnly: "    e = a + b\n" +
                        "    f = c + d\n" +
                        "\n" +
                        "    return e + f",
                docString: "text inside my *func* doc\n" +
                        "\n" +
                        "Parameters\n" +
                        "----------\n" +
                        "a: String\n" +
                        "  description"
        ]

        parsed.findEntryByName("Animal").should == [
                name: "Animal",
                type: "class",
                content: "class Animal:\n" +
                        "    \"\"\"\n" +
                        "    animal top level class doc string\n" +
                        "    \"\"\"\n" +
                        "\n" +
                        "    def says(self):\n" +
                        "        \"\"\"\n" +
                        "        animal talks\n" +
                        "        \"\"\"\n" +
                        "        print(\"hello\")",
                bodyOnly: "    def says(self):\n" +
                        "        \"\"\"\n" +
                        "        animal talks\n" +
                        "        \"\"\"\n" +
                        "        print(\"hello\")",
                docString: "animal top level class doc string"
        ]

        parsed.findEntryByName("one_line_var").should == [
            name: "one_line_var",
            type: "assignment",
            content: "one_line_var = \"one line variable assignment\"",
            bodyOnly: "\"one line variable assignment\"",
            docString: ""
        ]

        parsed.findEntryByName("multi_line_var").should == [
            name: "multi_line_var",
            type: "assignment",
            content: "multi_line_var = {\n" +
                "    \"line1\": \"first line\",\n" +
                "    \"line2\": \"second line\",\n" +
                "}",
            bodyOnly: "{\n" +
                "    \"line1\": \"first line\",\n" +
                "    \"line2\": \"second line\",\n" +
                "}",
            docString: ""
        ]

        parsed.findEntryByName("MyClass.V").should == [
            name: "MyClass.V",
            type: "assignment",
            content: "MyClass.V = 2",
            bodyOnly: "2",
            docString: ""
        ]

        parsed.findEntryByName("ADataClass").should == [
            name: "ADataClass",
            type: "class",
            content: "class ADataClass:\n    foo: str = \"bar\"",
            bodyOnly: "    foo: str = \"bar\"",
            docString: ""
        ]

        parsed.findEntryByName("ADataClassWithDocString").should == [
            name: "ADataClassWithDocString",
            type: "class",
            content: "class ADataClassWithDocString:\n    \"\"\"\n    A data class with a doc string.\n    \"\"\"\n    foo: str = \"bar\"",
            bodyOnly: "    foo: str = \"bar\"",
            docString: "A data class with a doc string."
        ]
    }
}
