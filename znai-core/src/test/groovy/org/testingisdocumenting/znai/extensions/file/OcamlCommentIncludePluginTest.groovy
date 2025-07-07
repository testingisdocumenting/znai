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

package org.testingisdocumenting.znai.extensions.file

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class OcamlCommentIncludePluginTest {
    @Test
    void "should extract multi-line comment block"() {
        def result = process("test-ocaml.ml", "{commentLine: '@param a the first integer'}")
        println result
        
    }

    @Test
    void "should extract single line comment"() {
        def result = process("test-ocaml.ml", "{commentLine: 'Single line comment'}")
        
        result.should == "Single line comment"
    }

    @Test
    void "should extract complex multi-line comment block"() {
        def result = process("test-ocaml.ml", "{commentLine: 'This function calculates the factorial of a number'}")
        
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
            process("test-ocaml.ml", "{commentLine: 'non-existent comment'}")
        } should throwException("Comment line not found: non-existent comment")
    }

    private static def process(fileName, params) {
        return PluginsTestUtils.processIncludeAndGetProps(":ocaml-comment: $fileName $params")
    }
}