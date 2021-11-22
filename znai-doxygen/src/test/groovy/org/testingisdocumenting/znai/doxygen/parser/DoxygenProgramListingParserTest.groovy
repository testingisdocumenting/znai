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

package org.testingisdocumenting.znai.doxygen.parser

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.utils.XmlUtils

class DoxygenProgramListingParserTest {
    @Test
    void "parse listing as simple code block"() {
        def codeBlock = parseProgramListing("doxygen-codeblock.xml")

        codeBlock.extension.should == "cpp"
        codeBlock.code.should == "template<typename T>\n" +
                "void println(const T& v) {\n" +
                "    std::cout << v << \"\\n\";\n" +
                "}\n" +
                "\n" +
                "template<typename T1, typename T2>\n" +
                "void multi_println(const T1& v1, const T2& v2) {\n" +
                "    std::cout << v1 << \", \" << v2 << \"\\n\";\n" +
                "}\n"
    }

    @Test
    void "no extension"() {
        def codeBlock = parseProgramListing("doxygen-codeblock-no-extension.xml")
        codeBlock.extension.should == ""
    }

    private static DoxygenCodeBlockSimple parseProgramListing(String resourceName) {
        def listingRoot = XmlUtils.anyNestedNodeByName(
                XmlUtils.parseXml(ResourceUtils.textContent(resourceName)),
                "programlisting")

        return DoxygenProgramListingParser.parseAsSimpleCodeBlock(listingRoot)
    }
}
