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

package com.twosigma.znai.extensions.json

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JsonIncludePluginTest {
    @Test
    void "should display full json"() {
        def elements = process('test.json')
        elements.should == [data : [key1: 'value1', key2: [key21: 'value21',
                                                           key22: 'value22']],
                            paths: [],
                            type : 'Json']
    }

    @Test
    void "single paths value is automatically converted to list"() {
        def elements = process('test.json {paths: "root.dat"}')
        elements.should == [data : [key1: 'value1', key2: [key21: 'value21',
                                                           key22: 'value22']],
                            paths: ['root.dat'],
                            type : 'Json']
    }

    @Test
    void "should read paths from file when provided"() {
        def elements = process('test.json {pathsFile: "jsonFileWithPaths.json"}')
        elements.should == [data     : [key1: 'value1', key2: [key21: 'value21',
                                                               key22: 'value22']],
                            pathsFile: 'jsonFileWithPaths.json',
                            paths    : ['root.dat1', 'root.dat2'],
                            type     : 'Json']
    }

    @Test
    void "should display subset of json"() {
        def elements = process('test.json {include: "$.key2"}')
        elements.should == [data   : [key21: 'value21',
                                      key22: 'value22'],
                            paths  : [],
                            include: '$.key2',
                            type   : 'Json']
    }

    @Test
    void "auxiliary files should include pathsFile"() {
        def auxiliaryFilesStream =
                PluginsTestUtils.processAndGetAuxiliaryFiles(
                        ':include-json: test.json {pathsFile: "jsonFileWithPaths.json"}')

        auxiliaryFilesStream.collect { af -> af.path.fileName.toString() }
                .should == ['jsonFileWithPaths.json', 'test.json']
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-json: $params")
        return result[0].toMap()
    }
}
