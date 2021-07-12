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

package org.testingisdocumenting.znai.python.pydoc

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PythonDocPandasLikeParamsParserTest {
    @Test
    void "parse parameters"() {
        def content = ResourceUtils.textContent("pydoc-pandas-like-example.txt")
        def parser = createParser()
        parser.handles(content).should == true
        def params = parser.parse(content)

        params.name.should == ["myName", "anotherName"]
        params.type.should == ["myType or None", "anotherType or Nil"]

        params[0].pyDocText.should == "text of myName param description\n" +
                "\n" +
                "with empty lines in between"

        params[1].pyDocText.should == "more textual description"
    }

    @Test
    void "empty parameters block"() {
        def parser = createParser()
        def params = parser.parse("Parameters\n__________")

        params.should == []
    }

    @Test
    void "should error on parameters header absence"() {
        def parser = createParser()
        code {
            parser.parse("")
        } should throwException("Can't find block with Parameters with underscore")
    }

    private static PythonDocPandasLikeParamsParser createParser() {
        return new PythonDocPandasLikeParamsParser().create()
    }
}
