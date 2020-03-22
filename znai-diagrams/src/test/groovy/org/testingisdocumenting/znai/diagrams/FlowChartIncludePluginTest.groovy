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

package org.testingisdocumenting.znai.diagrams

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.After
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Matchers.code
import static com.twosigma.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class FlowChartIncludePluginTest {
    @Before
    @After
    void init() {
        TEST_COMPONENTS_REGISTRY.docStructure().clearValidLinks()
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
        TEST_COMPONENTS_REGISTRY.docStructure().addValidLink(link)
    }
}
