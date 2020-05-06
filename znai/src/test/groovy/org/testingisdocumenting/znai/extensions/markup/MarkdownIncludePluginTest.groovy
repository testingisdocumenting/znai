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

package org.testingisdocumenting.znai.extensions.markup

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class MarkdownIncludePluginTest {
    @Test
    void "should register passed file as auxiliary file"() {
        def auxiliaryFilesStream = PluginsTestUtils.processAndGetAuxiliaryFiles(
                        ':include-markdown: test.md')

        auxiliaryFilesStream.collect { af -> af.path.fileName.toString() }.should == ['test.md']
    }

    @Test
    void "should pick first available file to include"() {
        def includePluginAndParserHandler = PluginsTestUtils.processAndGetPluginAndParserHandler(':include-markdown: ' +
                '{firstAvailable: ["non-existing.md", "test-optional.md"]}')
        includePluginAndParserHandler.includePlugin
                .auxiliaryFiles().collect { af -> af.path.fileName.toString() }.should == ['test-optional.md']

        includePluginAndParserHandler.parserHandler.docElement
                .toMap().content[0].markup.should == '# sample of optional instructions\n' +
                '\n' +
                'extra steps markdown is *simple*'
    }

    @Test
    void "should validate presence of a parameter"() {
        code {
            PluginsTestUtils.processAndGetIncludePlugin(
                    ':include-markdown:')
        } should throwException("use either <firstAvailable> or free form param to specify file to include," +
                " but none was specified")
    }

    @Test
    void "should only allow either firstAvailable or free form param but not both"() {
        code {
            PluginsTestUtils.processAndGetIncludePlugin(
                    ':include-markdown: test.md {firstAvailable: ["a.md", "b.md"]}')
        } should throwException("use either <firstAvailable> or free form param " +
                "to specify file to include, but not both")
    }
}
