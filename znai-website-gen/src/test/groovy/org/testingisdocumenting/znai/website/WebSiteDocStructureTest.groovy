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

import org.testingisdocumenting.znai.parser.PageSectionIdTitle
import org.testingisdocumenting.znai.structure.DocMeta
import org.testingisdocumenting.znai.structure.DocUrl
import org.testingisdocumenting.znai.structure.TableOfContents
import org.testingisdocumenting.znai.website.markups.MarkdownParsingConfiguration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class WebSiteDocStructureTest {
    static DocMeta docMeta
    static TableOfContents toc

    WebSiteDocStructure docStructure

    @BeforeClass
    static void init() {
        docMeta = new DocMeta([:])
        docMeta.setId('product')

        toc = new TableOfContents()
        toc.addTocItem('chapter', 'pageOne')
        toc.addTocItem('chapter', 'pageTwo')
        toc.addIndex("Product")
        toc.findTocItem('chapter', 'pageTwo').pageSectionIdTitles = [new PageSectionIdTitle ('Test Section')]
    }

    @Before
    void reCreateDocStructure() {
        docStructure = new WebSiteDocStructure(TEST_COMPONENTS_REGISTRY, docMeta, toc, new MarkdownParsingConfiguration())
    }

    @Test
    void "should create full url based on url type"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')

        docStructure.createUrl(path, new DocUrl("http://abc")).should == "http://abc"
        docStructure.createUrl(path, new DocUrl("https://abc")).should == "https://abc"
        docStructure.createUrl(path, new DocUrl("mailto:/link")).should == "mailto:/link"
        docStructure.createUrl(path, new DocUrl("file:/link")).should == "file:/link"
        docStructure.createUrl(path, new DocUrl("/")).should == "/product"
        docStructure.createUrl(path, new DocUrl("#anchor")).should == "/product/chapter/pageOne#anchor"
        docStructure.createUrl(path, new DocUrl("/#anchor")).should == "/product#anchor"
        docStructure.createUrl(path, new DocUrl("test/page")).should == "/product/test/page"
        docStructure.createUrl(path, new DocUrl("mailto/page")).should == "/product/mailto/page"
        docStructure.createUrl(path, new DocUrl("test/page#anchor")).should == "/product/test/page#anchor"
        docStructure.createUrl(path, new DocUrl("file-system/page")).should == "/product/file-system/page"
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
        docStructure.validateUrl(pageTwoPath, 'section title', new DocUrl('#test-section'))
        docStructure.validateCollectedLinks()
    }

    @Test
    void "should handle index page with anchor as forward slash"() {
        def indexPath = Paths.get('/home/user/docs/index.md')
        def referringPagePath = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.registerLocalAnchor(indexPath, 'index-id')
        docStructure.validateUrl(referringPagePath, 'section title: referring title', new DocUrl('/#index-id'))
        docStructure.validateCollectedLinks()
    }

    @Test
    void "should validate index page anchors"() {
        def indexPath = Paths.get('/home/user/docs/index.md')
        def referringPagePath = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.registerLocalAnchor(indexPath, 'index-id')
        docStructure.validateUrl(referringPagePath, 'section title: referring title', new DocUrl('/#index-wrong-id'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: /#index-wrong-id\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: referring title\n")
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
    void "should reject link that has no associated anchor"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.validateUrl(path, 'section title: section title', new DocUrl('chapter/pageOne#wrongRefId'))
        docStructure.validateUrl(path, 'section title: section title', new DocUrl('#anotherWrongRefId'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException('can\'t find a page associated with: chapter/pageOne#wrongRefId\n' +
                'check file: /home/user/docs/chapter/pageOne.md, section title: section title\n' +
                '\n' +
                'can\'t find the anchor #anotherWrongRefId\n' +
                'check file: /home/user/docs/chapter/pageOne.md, section title: section title\n')
    }
}
