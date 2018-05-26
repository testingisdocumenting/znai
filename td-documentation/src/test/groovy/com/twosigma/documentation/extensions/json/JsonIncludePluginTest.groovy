package com.twosigma.documentation.extensions.json

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal
import static java.util.stream.Collectors.toList

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
    void "single paths value is automatically converted to list"() {
        def elements = process('test.json {paths: "root.dat"}')
        actual(elements).should equal([data: [key1: 'value1', key2: [key21: 'value21',
                                                                     key22: 'value22']],
                                       paths: ['root.dat'],
                                       type: 'Json'])
    }

    @Test
    void "should read paths from file when provided"() {
        def elements = process('test.json {pathsFile: "jsonFileWithPaths.json"}')
        actual(elements).should equal([data: [key1: 'value1', key2: [key21: 'value21',
                                                                     key22: 'value22']],
                                       pathsFile: 'jsonFileWithPaths.json',
                                       paths: ['root.dat1', 'root.dat2'],
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

    @Test
    void "auxiliary files should include pathsFile"() {
        def auxiliaryFilesStream =
                PluginsTestUtils.processAndGetAuxiliaryFiles(':include-json: test.json {pathsFile: "jsonFileWithPaths.json"}')

        auxiliaryFilesStream
                .map { af -> af.path.fileName.toString() }
                .collect(toList()).should == ['jsonFileWithPaths.json', 'test.json']
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-json: $params")
        return result[0].toMap()
    }
}
