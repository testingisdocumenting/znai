package com.twosigma.documentation.website

import com.twosigma.documentation.parser.PageSectionIdTitle
import com.twosigma.documentation.structure.DocMeta
import com.twosigma.documentation.structure.DocUrl
import com.twosigma.documentation.structure.TableOfContents
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

class WebSiteDocStructureTest {
    static DocMeta docMeta
    static TableOfContents toc

    WebSiteDocStructure docStructure

    @BeforeClass
    static void init() {
        docMeta = new DocMeta([id: 'product'])

        toc = new TableOfContents('md')
        toc.addTocItem('chapter', 'pageOne')
        toc.addTocItem('chapter', 'pageTwo')
        toc.findTocItem('chapter', 'pageTwo').pageSectionIdTitles = [new PageSectionIdTitle ('Test Section')]
    }

    @Before
    void reCreateDocStructure() {
        docStructure = new WebSiteDocStructure(docMeta, toc)
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
        docStructure.validateUrl(path, 'section title', new DocUrl('chapter/unknown-page'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/unknown-page\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: section title\n")
    }

    @Test
    void "should reject link that has no associated global anchor"() {
        def path = Paths.get('/home/user/docs/chapter/pageOne.md')
        docStructure.validateUrl(path, 'section title', new DocUrl('chapter/pageOne#wrongRefId'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/pageOne#wrongRefId\n" +
                "check file: /home/user/docs/chapter/pageOne.md, section title: section title\n")
    }
}
