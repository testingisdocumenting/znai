package com.twosigma.znai.typescript

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class TypeScriptIncludePluginTest {
    @Test
    void "should extract jsx declarations"() {
        def elements = process('src/test/resources/Sample.tsx {jsxElementsFrom: "demo"}')

        elements.should == [[type: 'JsxGroup', declarations: [
                [tagName: 'Declaration', attributes: [[name: 'firstName', value: '"placeholder"'],
                                                      [name: 'lastName', value: '{this.lastName}']]]]]]
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-typescript: $params")
        return result*.toMap()
    }
}
