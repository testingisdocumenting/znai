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

package com.twosigma.znai.website

import com.twosigma.znai.parser.PageSectionIdTitle
import com.twosigma.znai.structure.DocMeta
import com.twosigma.znai.structure.DocUrl
import com.twosigma.znai.structure.TableOfContents
import com.twosigma.znai.website.markups.MarkdownParsingConfiguration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.Matchers.code
import static com.twosigma.webtau.Matchers.throwException
import static com.twosigma.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class WebSiteDocStructureTest {
    static DocMeta docMeta
    static TableOfContents toc

    WebSiteDocStructure docStructure

    @BeforeClass
    static void init() {
        docMeta = new DocMeta([id: 'product'])

        toc = new TableOfContents()
        toc.addTocItem('chapter', 'pageOne')
        toc.addTocItem('chapter', 'pageTwo')
        toc.findTocItem('chapter', 'pageTwo').pageSectionIdTitles = [new PageSectionIdTitle ('Test Section')]
    }

    @Before
    void reCreateDocStructure() {
        docStructure = new WebSiteDocStructure(TEST_COMPONENTS_REGISTRY, docMeta, toc, new MarkdownParsingConfiguration())
    }

    @Test
    void "should accept direct links to anchors as long as anchors are registered"() {
        def pageOnePath = Paths.get('/home/user/docs/chapter/pageOne.md')
        def pageTwoPath = Paths.get('/home/user/docs/chapter/pageTwo.md')
        docStructure.registerGlobalAnchor(pageOnePath, 'functionRefId')
        docStructure.registerLocalAnchor(pageOnePath, 'localId')
        docStructure.registerLocalAnchor(pageTwoPath, 'test-section')
        docStructure.validateUrl(pageOnePath, 'section title', new DocUrl('chapter/pageOne#functionRefId'))
        docStructure.validateUrl(pageOnePath, 'section title', new DocUrl('chapter/pageOne#localId'))
        docStructure.validateUrl(pageOnePath, 'section title', new DocUrl('chapter/pageTwo#test-section'))
        docStructure.validateCollectedLinks()
    }

    @Test
    void "should reject link that has no associated toc item"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.validateUrl(path, 'section title: section title', new DocUrl('chapter/unknown-page'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/unknown-page\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: section title\n")
    }

    @Test
    void "should reject link that has no associated global anchor"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.validateUrl(path, 'section title: section title', new DocUrl('chapter/pageOne#wrongRefId'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/pageOne#wrongRefId\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: section title\n")
    }
}
