package com.twosigma.documentation.extensions.markup

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

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
