/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.extensions.file

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class SnippetHighlightFeatureTest {
    @Test
    void "validate indexes out of bound"() {
        code {
            createAndRunFeature([highlight: 3], "hello world")
        } should throwException("highlight idx is out of range: 3\n" +
                "check: test-snippet")
    }

    @Test
    void "validate missing match"() {
        code {
            createAndRunFeature([highlight: "hallo"], "hello\nworld")
        } should throwException("highlight text <hallo> is not found\n" +
                "check: test-snippet\nhello\nworld")
    }

    @Test
    void "validate missing region start value"() {
        code {
            createAndRunFeature([highlightRegion: [:]], "")
        } should throwException("highlightRegion.start is missing")
    }

    @Test
    void "validate missing region end value"() {
        code {
            createAndRunFeature([highlightRegion: [start: "hello"]], "")
        } should throwException("highlightRegion.end is missing")
    }

    @Test
    void "validate region start and end wrong type"() {
        code {
            createAndRunFeature([highlightRegion: [start: [:], end: [:]]], "")
        } should throwException("highlightRegion should be in format {start: \"line-star\", end: \"line-end\"}")
    }

    @Test
    void "highlight by index and substring"() {
        def props = createAndRunFeature([highlight: ["llo", 1]], "hello\nworld")
        props.should == ["highlight": [0, 1]]
    }

    @Test
    void "highlight by region"() {
        def props = createAndRunFeature([highlightRegion: [start: "world", end: "and"]], "hello\nworld\nof\ndocumentation\nand\ntests")
        props.should == ["highlight": [1, 2, 3, 4]]
    }

    @Test
    void "remove highlight duplicates"() {
        def props = createAndRunFeature([highlight: ["llo", 1, "world"]], "hello\nworld")
        props.should == ["highlight": [0, 1]]
    }

    private static Map<String, Object> createAndRunFeature(Map params, String content) {
        def snippetContentProvider = createSnippet(content)
        def pluginParams = createParams(params)

        def feature = new SnippetHighlightFeature(TEST_COMPONENTS_REGISTRY, pluginParams, snippetContentProvider)
        Map<String, Object> props = [:]
        feature.updateProps(props)

        return props
    }

    private static PluginParams createParams(Map params) {
        return TEST_COMPONENTS_REGISTRY.pluginParamsFactory().create("file", "", params)
    }

    private static SnippetContentProvider createSnippet(String content) {
        return new SnippetContentProvider() {
            @Override
            String snippetContent() {
                return content
            }

            @Override
            String snippetId() {
                return "test-snippet"
            }
        }
    }
}
