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

package org.testingisdocumenting.znai.extensions.json

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class JsonIncludePluginTest {
    private def expectedFullData = [key1: 'value1', key2: [key21: 'value21', key22: 'value22']]

    @Test
    void "should display full json"() {
        def props = process('test.json')
        props.should == [data: expectedFullData, highlightValues: [], highlightKeys: []]
    }

    @Test
    void "should handle null values"() {
        def props = process('with-null.json')
        props.should == [data: [key1: null, key2: [key21: 'value21', key22: 'value22']], highlightValues: [], highlightKeys: []]
    }

    @Test
    void "single paths value is automatically converted to list"() {
        def props = process('test.json {highlightValue: "root.key1"}')
        props.should == [data: expectedFullData, highlightValues: ['root.key1'], highlightKeys: []]
    }

    @Test
    void "should allow old name for highlightValue to be used"() {
        def props = process('test.json {highlightValue: "root.key1"}')
        props.should == [data: expectedFullData, highlightValues: ['root.key1'], highlightKeys: []]
    }

    @Test
    void "should read paths from file when provided"() {
        def props = process('test.json {highlightValueFile: "jsonFileWithPaths.json"}')
        props.should == [data           : expectedFullData,
                         highlightValues: ['root.key1', 'root.key2'],
                         highlightKeys  : []]
    }

    @Test
    void "should read paths from file using deprecated name when provided"() {
        def props = process('test.json {pathsFile: "jsonFileWithPaths.json"}')
        props.should == [data           : expectedFullData,
                         highlightValues: ['root.key1', 'root.key2'],
                         highlightKeys  : []]
    }

    @Test
    void "should read keys to highlight from file when provided"() {
        def props = process('test.json {highlightKeyFile: "jsonFileWithPaths.json"}')
        props.should == [data           : expectedFullData,
                         highlightKeys  : ['root.key1', 'root.key2'],
                         highlightValues: []]
    }

    @Test
    void "should display subset of json"() {
        def props = process('test.json {include: "$.key2"}')
        props.should == [data           : [key21: 'value21',
                                           key22: 'value22'],
                         highlightValues: [], highlightKeys: [],
                         include        : '$.key2']
    }

    @Test
    void "should validate paths presence"() {
        code {
            process('test.json {highlightValue: ["root.key_1", "root.key_2"]}')
        } should throwException("can't find highlightValue: root.key_1 in JSON, available:\n" +
                "  root\n" +
                "  root.key1\n" +
                "  root.key2\n" +
                "  root.key2.key21\n" +
                "  root.key2.key22")
    }

    @Test
    void "should validate keys to highlight presence"() {
        code {
            process('test.json {highlightKey: ["root.key_1", "root.key_2"]}')
        } should throwException("can't find highlightKey: root.key_1 in JSON, available:\n" +
                "  root\n" +
                "  root.key1\n" +
                "  root.key2\n" +
                "  root.key2.key21\n" +
                "  root.key2.key22")
    }

    @Test
    void "should validate collapsed paths presence"() {
        code {
            process('test.json {collapsedPaths: ["root.key_1", "root.key_2"]}')
        } should throwException("can't find collapsedPaths: root.key_1 in JSON, available:\n" +
                "  root\n" +
                "  root.key1\n" +
                "  root.key2\n" +
                "  root.key2.key21\n" +
                "  root.key2.key22")
    }

    @Test
    void "should auto title"() {
        def props = process('test.json {"autoTitle": true}')
        props.should == [data           : expectedFullData,
                         autoTitle      : true,
                         title          : "test.json",
                         anchorId       : "test-json",
                         highlightValues: [],
                         highlightKeys  : []]
    }

    @Test
    void "auxiliary files should include highlight value file"() {
        def auxiliaryFilesStream =
                PluginsTestUtils.processIncludeAndGetAuxiliaryFiles(
                        ':include-json: test.json {highlightValueFile: "jsonFileWithPaths.json"}')

        auxiliaryFilesStream.collect { af -> af.path.fileName.toString() }
                .should == ['jsonFileWithPaths.json', 'test.json']
    }

    @Test
    void "should maintain original json order"() {
        def props = process('test-account.json')
        props.data.keySet().should == ["id", "price", "amount"]
    }

    @Test
    void "should parse callouts markdown"() {
        def props = process('test-account.json {callouts: {"root.id": "hello *world*", "root.price": "`code`"}}')

        props.calloutsByPath.should == [
            "root.id": [["markdown": "hello *world*", "type": "TestMarkdown"]],
            "root.price": [["markdown": "`code`", "type": "TestMarkdown"]]
        ]
    }

    @Test
    void "should validate callouts paths"() {
        code {
            process('test-account.json {callouts: {"root.tid": "hello *world*", "root.price": "`code`"}}')
        } should throwException("can't find callouts: root.tid in JSON, available:\n" +
                "  root\n" +
                "  root.id\n" +
                "  root.price\n" +
                "  root.amount")
    }

    @Test
    void "should parse callouts from json file"() {
        def props = process('test-account.json {calloutsFile: "json-callouts.json"}}')

        props.calloutsByPath.should == [
                "root.id": [["markdown": "root id", "type": "TestMarkdown"]],
                "root.price": [["markdown": "root *price*", "type": "TestMarkdown"]]
        ]
    }

    @Test
    void "should parse callouts from csv file"() {
        def props = process('test-account.json {calloutsFile: "json-callouts.csv"}}')

        props.calloutsByPath.should == [
                "root.id": [["markdown": "root id", "type": "TestMarkdown"]],
                "root.price": [["markdown": "root *price*", "type": "TestMarkdown"]]
        ]
    }

    private static def process(String params) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-json: $params")
    }
}
