package com.twosigma.znai.diagrams

import com.twosigma.znai.extensions.include.PluginsTestUtils
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

class FlowChartIncludePluginTest {
    @Before
    @After
    void init() {
        TestComponentsRegistry.INSTANCE.getValidator().clearValidLinks()
    }

    @Test
    void "should convert user provided links"() {
        registerValidLink('doc/page')
        registerValidLink('ref/another')

        def result = process('diagram-with-urls.json')
        result.urls.should == [n1: '/test-doc/doc/page', n2: '/test-doc/ref/another']
    }

    @Test
    void "should report invalid links urls"() {
        registerValidLink('ref/another')

        code {
            process('diagram-with-urls.json')
        } should throwException(~/no valid link.*doc\/page/)
    }

    static def process(fileName) {
        return PluginsTestUtils.process(":include-flow-chart: $fileName")[0].toMap()
    }

    static void registerValidLink(link) {
        TestComponentsRegistry.INSTANCE.getValidator().addValidLink(link)
    }
}
