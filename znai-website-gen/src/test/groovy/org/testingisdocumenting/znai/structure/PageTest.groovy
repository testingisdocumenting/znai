/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.structure

import org.junit.Test
import org.testingisdocumenting.znai.parser.MarkupParser
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.testingisdocumenting.znai.parser.docelement.DocElement

import java.nio.file.Paths
import java.time.Instant

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PageTest {
    static final TestComponentsRegistry componentsRegistry = TEST_COMPONENTS_REGISTRY
    static final MarkupParser parser = new MarkdownParser(componentsRegistry)
    DocElement docElement

    @Test
    void "should use custom anchor ids"() {
        parse("""# section one {#s1} 
hello

# section two {#s2}

world

# section three

of markdown
""")
        def page = new Page(docElement, Instant.now(), new PageMeta())
        page.getPageSectionIdTitles().should == [ "id" | "title"] {
                                               _____________________
                                                  "s1" | "section one"
                                                  "s2" | "section two"
                                       "section-three" | "section three" }
    }

    private void parse(String markdown) {
        def parseResult = parser.parse(Paths.get("pagetest.md"), markdown)
        docElement = parseResult.docElement()
    }
}
