/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.extensions.include.IncludePlugin
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JavaDocIncludePluginTest {
    @Test
    void "method in doc with signature"() {
        def plugin = processAndGetPlugin('{entry: "sampleMethod(String, List)"}')
        plugin.textForSearch().text.should == ['overloaded method java doc']
    }

    @Test
    void "method in doc without signature"() {
        def plugin = processAndGetPlugin('{entry: "sampleMethod"}')
        plugin.textForSearch().text.should == ['method level java doc package Class']
    }

    @Test
    void "handling of java doc with markdown"() {
        def pluginAndParserHandler = processAndGetProps('{entry: "myOtherField", markdown: true}')
        println pluginAndParserHandler.parserHandler.docElement.contentToListOfMaps()
        pluginAndParserHandler.parserHandler.docElement.contentToListOfMaps().should == [
                [markdown: 'outer *field* description', type: 'TestMarkdown'],
                [code: 'package.Class', type: 'InlinedCode'],
                [markdown: 'some text\n* bullet one\n* bullet two', type: 'TestMarkdown']]

        // the rest of search text is not present due to TestMarkdownParser ignores fine grained elements and therefore
        // SearchCrawler doesn't get enough info.
        // textForSearch is fully tested in HtmlToDocElementConverterTest
        pluginAndParserHandler.includePlugin.textForSearch().text.should == ['package Class']
    }

    @Test
    void "method in doc inner class"() {
        def plugin = processAndGetPlugin('{entry: "InnerClass.sampleMethod"}')
        plugin.textForSearch().text.should == ['inner class method level java doc package Class']
    }

    @Test
    void "static variable in doc"() {
        def plugin = processAndGetPlugin('{entry: "SAMPLE_CONST"}')
        plugin.textForSearch().text.should == ['variable level java doc']
    }

    @Test
    void "variable in doc with clashing name in inner class"() {
        def plugin = processAndGetPlugin('{entry: "myField"}')
        plugin.textForSearch().text.should == ['outer field description']
    }

    @Test
    void "variable in doc with inner class name qualifier"() {
        def plugin = processAndGetPlugin('{entry: "InnerClass.myField"}')
        plugin.textForSearch().text.should == ['inner field description']
    }

    @Test
    void "variable with a link in doc"() {
        def plugin = processAndGetPlugin('{entry: "SAMPLE_CONST_WITH_LINK"}')
        plugin.textForSearch().text.should == ['variable level java doc package Class']
    }

    private static IncludePlugin processAndGetPlugin(String params) {
        return PluginsTestUtils.processAndGetIncludePlugin(fullPluginParams(params))
    }

    private static PluginsTestUtils.IncludePluginAndParserHandler processAndGetProps(String params) {
        return PluginsTestUtils.processAndGetIncludePluginAndParserHandler(fullPluginParams(params))
    }

    private static String fullPluginParams(String params) {
        return ":include-java-doc: WithJavaDocs.java " + params
    }
}
