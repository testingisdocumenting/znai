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

package org.testingisdocumenting.znai.extensions.image

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.greaterThan
import static org.testingisdocumenting.webtau.Matchers.greaterThanOrEqual

class ImageFencePluginTest {
    @Test
    void "should generate badge shapes from fence block text"() {
        def handler = PluginsTestUtils.processAndGetFencePluginAndParserHandler(
                new PluginParams('image', 'dummy.png', [scale: 2.0d]),
                "100, 200\n" +
                        "150, 40\n").parserHandler

        def props = handler.docElement.contentToListOfMaps()
        props.should == [[imageSrc: '/test-doc/dummy.png',
                          shapes: [[x: 100.0, y: 200.0, text: "1", type: "badge", invertedColors: false],
                                   [x: 150.0, y: 40.0, text: "2", type: "badge", invertedColors: false]],
                          timestamp: greaterThanOrEqual(0),
                          width: greaterThan(0),
                          height: greaterThan(0),
                          scale: 2.0,
                          type: "AnnotatedImage"]]
    }

    @Test
    void "should generate rect and arrows shapes from fence block text"() {
        def handler = PluginsTestUtils.processAndGetFencePluginAndParserHandler(
                new PluginParams('image', 'dummy.png', [scale: 2.0d]),
                "rect,100, 200, 140, 240\n" +
                        "arrow, 150, 40, 190, 80, hello world \n").parserHandler

        def props = handler.docElement.contentToListOfMaps()
        props.should == [[imageSrc: '/test-doc/dummy.png',
                          shapes: [
                                  [beginX: 100.0, beginY: 200.0, endX: 140.0, endY: 240.0, type: "rectangle", invertedColors: false],
                                  [beginX: 150.0, beginY: 40.0, endX: 190.0, endY: 80.0, type: "arrow", tooltip: [[markdown: "hello world", type: "TestMarkdown"]], invertedColors: false]],
                          timestamp: greaterThanOrEqual(0),
                          width: greaterThan(0),
                          height: greaterThan(0),
                          scale: 2.0,
                          type: "AnnotatedImage"]]
    }
}
