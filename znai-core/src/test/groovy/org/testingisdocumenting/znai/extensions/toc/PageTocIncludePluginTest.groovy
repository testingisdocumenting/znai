/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.toc

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.testingisdocumenting.znai.parser.PageSectionIdTitle
import org.testingisdocumenting.znai.structure.TableOfContents

import java.nio.file.Path
import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PageTocIncludePluginTest {
    TableOfContents prevToc
    TableOfContents toc

    @Before
    void setup() {
        prevToc = TEST_COMPONENTS_REGISTRY.docStructure().tableOfContents()
        toc = new TableOfContents()
        TEST_COMPONENTS_REGISTRY.docStructure().setToc(toc)

        toc.addIndex("my doc")
        def tocItem = toc.addTocItem("chapter-one", "page-one")
        tocItem.setPageSectionIdTitles([
                new PageSectionIdTitle("Section One"),
                new PageSectionIdTitle("Section Two")
        ])
    }

    @After
    void restore() {
        TEST_COMPONENTS_REGISTRY.docStructure().setToc(prevToc)
    }

    @Test
    void "should generate lazy evaluated props"() {
        def props = resultingProps(Paths.get("/home/my-dir/chapter-one/page-one.md"))
        def supplier = props.get("sections")
        supplier.get().should == [[title: "Section One", id: "section-one"],
                                  [title: "Section Two", id: "section-two"]]
    }

    @Test
    void "should throw when markup path doesn't match any toc item"() {
        code {
            resultingProps(Paths.get("/home/my-dir/chapter-one/page-wrong-name-somehow.md"))
        } should throwException("File is not part of TOC: /home/my-dir/chapter-one/page-wrong-name-somehow.md")
    }

    private static Map<String, Object> resultingProps(Path markupPath) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-page-toc:", markupPath)
    }
}
