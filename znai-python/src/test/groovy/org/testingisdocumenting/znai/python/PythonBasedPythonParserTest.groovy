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
        parsed.should == [
                ["type": "function", "name": "my_func", "doc_string": "text inside my func doc"],
                ["type": "function", "name": "another_func", "doc_string": "more diff text"],
                ["type": "class", "name": "Animal", "doc_string": "animal top level class doc string"],
                ["type": "function", "name": "Animal.says", "doc_string": "animal talks"]]
    }
}
