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

package org.testingisdocumenting.znai.website

import org.testingisdocumenting.znai.core.MarkupPathWithError
import org.testingisdocumenting.znai.parser.PageSectionIdTitle
import org.testingisdocumenting.znai.core.DocMeta
import org.testingisdocumenting.znai.structure.AnchorIds
import org.testingisdocumenting.znai.structure.DocUrl
import org.testingisdocumenting.znai.structure.TableOfContents
import org.testingisdocumenting.znai.website.markups.MarkdownParsingConfiguration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class WebSiteDocStructureTest {
    static DocMeta docMeta
    static TableOfContents toc

    static Path markupPath = Paths.get("dir-one/file-one.md")
    WebSiteDocStructure docStructure

    @BeforeClass
    static void init() {
        docMeta = new DocMeta([:])
        docMeta.setId("product")

        toc = new TableOfContents("md")
        toc.addTocItem("chapter", "pageOne")
        toc.addTocItem("chapter", "pageTwo")
        toc.addIndex()
        toc.findTocItem("chapter", "pageTwo").pageSectionIdTitles = [new PageSectionIdTitle ("Test Section", [:])]
        toc.resolveTocItemPathsAndReturnMissing { new MarkupPathWithError(Paths.get("/home/user/docs/" + it.getFilePath()), null) }
    }

    @Before
    void reCreateDocStructure() {
        docStructure = new WebSiteDocStructure(TEST_COMPONENTS_REGISTRY, docMeta, toc, new MarkdownParsingConfiguration())
    }

    @Test
    void "should create full url based on url type"() {
        def wrongPath = Paths.get('/home/user/docs/index-wrong.md')
        def indexPath = Paths.get('/home/user/docs/index.md')
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')

        docStructure.createUrl(path, new DocUrl(markupPath, "http://abc")).should == "http://abc"
        docStructure.createUrl(path, new DocUrl(markupPath, "https://abc")).should == "https://abc"
        docStructure.createUrl(path, new DocUrl(markupPath, "mailto:/link")).should == "mailto:/link"
        docStructure.createUrl(path, new DocUrl(markupPath, "file:/link")).should == "file:/link"
        docStructure.createUrl(path, new DocUrl(markupPath, "/")).should == "/product"
        docStructure.createUrl(path, new DocUrl(markupPath, "#anchor")).should == "/product/chapter/pageOne#anchor"
        docStructure.createUrl(path, new DocUrl(markupPath, "/#anchor")).should == "/product#anchor"
        docStructure.createUrl(path, new DocUrl(markupPath, "test/page")).should == "/product/test/page"
        docStructure.createUrl(path, new DocUrl(markupPath, "mailto/page")).should == "/product/mailto/page"
        docStructure.createUrl(path, new DocUrl(markupPath, "test/page#anchor")).should == "/product/test/page#anchor"
        docStructure.createUrl(path, new DocUrl(markupPath, "file-system/page")).should == "/product/file-system/page"
        docStructure.createUrl(indexPath, new DocUrl(markupPath, "#ref")).should == "/product#ref"
        docStructure.createUrl(indexPath, new DocUrl(markupPath, "file-system/page")).should == "/product/file-system/page"
        docStructure.createUrl(wrongPath, new DocUrl(markupPath, "file-system/page")).should == "/product/file-system/page"
    }

    @Test
    void "should accept direct links to anchors as long as anchors are registered"() {
        def pageOnePath = Paths.get('/home/user/docs/chapter/pageOne.md')
        def pageTwoPath = Paths.get('/home/user/docs/chapter/pageTwo.md')
        docStructure.registerGlobalAnchor(pageOnePath, 'functionRefId')
        docStructure.registerLocalAnchors(pageOnePath, new AnchorIds('localId', []))
        docStructure.registerLocalAnchors(pageTwoPath, new AnchorIds('test-section', []))
        docStructure.validateUrl(pageOnePath, 'section title', new DocUrl(markupPath, 'chapter/pageOne#functionRefId'))
        docStructure.validateUrl(pageOnePath, 'section title', new DocUrl(markupPath, 'chapter/pageOne#localId'))
        docStructure.validateUrl(pageOnePath, 'section title', new DocUrl(markupPath, 'chapter/pageTwo#test-section'))
        docStructure.validateUrl(pageTwoPath, 'section title', new DocUrl(markupPath, '#test-section'))
        docStructure.validateCollectedLinks()
    }

    @Test
    void "should handle index page with anchor as forward slash"() {
        def indexPath = Paths.get('/home/user/docs/index.md')
        def referringPagePath = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.registerLocalAnchors(indexPath, new AnchorIds('index-id', []))
        docStructure.validateUrl(referringPagePath, 'section title: referring title', new DocUrl(markupPath, '/#index-id'))
        docStructure.validateCollectedLinks()
    }

    @Test
    void "should validate index page anchors"() {
        def indexPath = Paths.get('/home/user/docs/index.md')
        def referringPagePath = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.registerLocalAnchors(indexPath, new AnchorIds('index-id', []))
        docStructure.validateUrl(referringPagePath, 'section title: referring title', new DocUrl(markupPath, '/#index-wrong-id'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: /#index-wrong-id\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: referring title\n")
    }

    @Test
    void "should reject link that has no associated toc item"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.validateUrl(path, 'section title: section title', new DocUrl(markupPath, 'chapter/unknown-page'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/unknown-page\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: section title\n")
    }

    @Test
    void "should reject link that has no associated anchor"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.validateUrl(path, 'section title: section title', new DocUrl(markupPath, 'chapter/pageOne#wrongRefId'))
        docStructure.validateUrl(path, 'section title: section title', new DocUrl(markupPath, '#anotherWrongRefId'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException('can\'t find a page associated with: chapter/pageOne#wrongRefId\n' +
                'check file: /home/user/docs/chapter/pageOne.md, section title: section title\n' +
                '\n' +
                'can\'t find the anchor #anotherWrongRefId\n' +
                'check file: /home/user/docs/chapter/pageOne.md, section title: section title\n')
    }
}
