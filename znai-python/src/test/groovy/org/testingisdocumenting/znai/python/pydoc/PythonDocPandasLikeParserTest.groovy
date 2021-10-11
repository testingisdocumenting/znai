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

class PythonDocPandasLikeParserTest {
    @Test
    void "parse parameters with underscore"() {
        parseAndValidateParams("pydoc-pandas-like-example-underscore.txt")
    }

    @Test
    void "parse parameters with dash as underscore"() {
        parseAndValidateParams("pydoc-pandas-like-example.txt")
    }

    @Test
    void "parse parameters with dash in name"() {
        def params = parse("pydoc-pandas-like-name-with-dashes.txt").params
        params.name.should == ["--my-param", "--another-param"]
        params.type.should == ["", ""]
    }

    @Test
    void "empty parameters block"() {
        def parser = createParser()
        def params = parser.parse("Parameters\n----------").params

        params.should == []
    }

    @Test
    void "should return empty list when empty or absent parameters header"() {
        def parser = createParser()
        parser.parse("").params.should == []
    }

    @Test
    void "should return description only ignoring params block"() {
        def expected = "My documentation text blah\n" +
                "in multiple lines when we include any text as py doc\n" +
                "we exclude all the future sections"

        parse("pydoc-pandas-like-example.txt").descriptionOnly.should == expected
        parse("pydoc-pandas-like-example-no-params.txt").descriptionOnly.should == expected
    }

    private static void parseAndValidateParams(String fileName) {
        def params = parse(fileName).params

        params.name.should == ["my_name", "another_name", "no_type"]
        params.type.should == ["myType or None", "anotherType or Nil", ""]

        params[0].pyDocText.should == "text of myName param description\n" +
                "\n" +
                "with empty lines in between"

        params[1].pyDocText.should == "more textual description"

        params[2].pyDocText.should == "no type param"
    }

    private static PythonDocParserResult parse(String fileName) {
        def content = ResourceUtils.textContent(fileName)
        def parser = createParser()
        parser.handles(content).should == true

        return parser.parse(content)
    }
    private static PythonDocPandasLikeParser createParser() {
        return new PythonDocPandasLikeParser().create()
    }
}
