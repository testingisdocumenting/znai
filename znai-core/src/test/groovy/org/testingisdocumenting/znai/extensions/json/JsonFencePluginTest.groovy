/*
 * Copyright 2022 znai maintainers
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

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class JsonFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    private String json = '{\n' +
            '  "key1": "value1",\n' +
            '  "key2": {"key21": "value21", "key22": "value22"}\n' +
            '}'

    private def expectedFullData = [key1: 'value1', key2: [key21: 'value21', key22: 'value22']]

    @Test
    void "should display full json"() {
        def props = process([:], json)
        props.should == [data: expectedFullData, highlightValues: [], highlightKeys: []]
    }

    @Test
    void "should support deprecated parameter 'paths'"() {
        def props = process([paths: "root.key1"], json)
        props.should == [data : expectedFullData,  highlightValues: ['root.key1'], highlightKeys: []]
    }

    @Test
    void "single paths value is automatically converted to list"() {
        def props = process([highlightValue: "root.key1"], json)
        props.should == [data : expectedFullData, highlightValues: ['root.key1'], highlightKeys: []]
    }

    @Test
    void "should display subset of json"() {
        def props = process([include: '$.key2'], json)
        props.should == [data   : [key21: 'value21',
                                   key22: 'value22'],
                         highlightValues: [],
                         highlightKeys: [],
                         include: '$.key2']
    }

    @Test
    void "should enclose in object with one element path"() {
        def props = process([encloseInObject: "book"], json)
        props.data.should == [book: [key1: "value1", key2: [key21: "value21", key22: "value22"]]]
    }

    @Test
    void "should enclose in object with multiple elements path"() {
        def props = process([encloseInObject: "book.relocation"], json)
        props.data.should == [book: [relocation: [key1: "value1", key2: [key21: "value21", key22: "value22"]]]]
    }

    @Test
    void "enclose in object path should not be empty"() {
        code {
            process([encloseInObject: "  "], json)
        } should throwException("encloseInObject can't be empty")
    }

    @Test
    void "should enclose after include"() {
        def props = process([include: '$.key2', encloseInObject: "filtered"], json)
        props.data.should == [filtered: [key21: 'value21',
                                         key22: 'value22']]
    }

    @Test
    void "auxiliary files should include pathsFile"() {
        def auxiliaryFilesStream =
                PluginsTestUtils.processFenceAndGetAuxiliaryFiles(pluginParamsFactory.create(
                        "json", "",
                        [pathsFile: "jsonFileWithPaths.json"]), json)

        auxiliaryFilesStream.collect { af -> af.path.fileName.toString() }
                .should == ['jsonFileWithPaths.json']
    }

    @Test
    void "should maintain original json order"() {
        def props = process([:], '{\n' +
                '  "id": "id1",\n' +
                '  "price": 100,\n' +
                '  "amount": 30\n' +
                '}')

        props.data.keySet().should == ["id", "price", "amount"]
    }

    private static def process(Map<String, ?> params, String content) {
        return PluginsTestUtils.processFenceAndGetProps(pluginParamsFactory.create("json", "", params), content)
    }
}
