package com.twosigma.documentation.extensions.json

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal

class JsonIncludePluginTest {
    @Test
    void "should display full json"() {
        def elements = process('test.json')
        actual(elements).should equal([data: [key1: 'value1', key2: [key21: 'value21',
                                                                     key22: 'value22']],
                                       paths:[],
                                       type: 'Json'])
    }

    @Test
    void "should display subset of json"() {
        def elements = process('test.json {include: "$.key2"}')
        actual(elements).should equal([data: [key21: 'value21',
                                              key22: 'value22'],
                                       paths:[],
                                       include: '$.key2',
                                       type: 'Json'])
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-json: $params")
        return result[0].toMap()
    }
}
