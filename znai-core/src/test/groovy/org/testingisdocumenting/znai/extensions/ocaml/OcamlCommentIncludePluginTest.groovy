/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.extensions.ocaml

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class OcamlCommentIncludePluginTest {
    @Test
    void "should extract comment for add function"() {
        def result = process("test-ocaml.ml", "{commentLine: 'let add'}")
        
        result.should == "This is a simple OCaml function"
    }

    @Test
    void "should extract multi-line comment for multiply function"() {
        def result = process("test-ocaml.ml", "{commentLine: 'let multiply'}")
        
        result.should == "This is a more complex function\n" +
                "that demonstrates multiple parameter handling\n" +
                "and return value documentation\n" +
                "\n" +
                "@param a the first integer\n" +
                "@param b the second integer\n" +
                "@return the sum of a and b"
    }

    @Test
    void "should extract single line comment for subtract function"() {
        def result = process("test-ocaml.ml", "{commentLine: 'let subtract'}")
        
        result.should == "Single line comment"
    }

    @Test
    void "should extract complex multi-line comment for factorial function"() {
        def result = process("test-ocaml.ml", "{commentLine: 'let rec factorial'}")
        
        result.should == "Multi-line comment block\n" +
                "with detailed documentation\n" +
                "\n" +
                "This function calculates the factorial of a number\n" +
                "using recursive approach.\n" +
                "\n" +
                "Example usage:\n" +
                "- factorial 5 returns 120\n" +
                "- factorial 0 returns 1"
    }

    @Test
    void "should throw exception when comment line not found"() {
        code {
            process("test-ocaml.ml", "{commentLine: 'non-existent-function'}")
        } should throwException("can't find text: non-existent-function")
    }

    private static def process(fileName, params) {
        return PluginsTestUtils.processIncludeAndGetProps(":ocaml-comment: $fileName $params")
    }
}