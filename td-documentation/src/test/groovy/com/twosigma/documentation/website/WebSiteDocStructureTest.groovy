package com.twosigma.documentation.website

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
        toc.addTocItem('chapter', 'page')
    }

    @Before
    void reCreateDocStructure() {
        docStructure = new WebSiteDocStructure(docMeta, toc)
    }

    @Test
    void "should accept direct links to anchors as long as anchors are registered"() {
        def path = Paths.get('/home/user/docs/chapter/page.md')
        docStructure.registerGlobalAnchor(path, 'functionRefId')
        docStructure.registerLocalAnchor(path, 'localId')
        docStructure.validateUrl(path, 'section title', new DocUrl('chapter/page#functionRefId'))
        docStructure.validateUrl(path, 'section title', new DocUrl('chapter/page#localId'))
        docStructure.validateCollectedLinks()
    }

    @Test
    void "should reject link that has no associated toc item"() {
        def path = Paths.get('/home/user/docs/chapter/page.md')
        docStructure.validateUrl(path, 'section title', new DocUrl('chapter/unknown-page'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/unknown-page\n" +
                "check file: /home/user/docs/chapter/page.md, section title: section title\n")
    }

    @Test
    void "should reject link that has no associated global anchor"() {
        def path = Paths.get('/home/user/docs/chapter/page.md')
        docStructure.validateUrl(path, 'section title', new DocUrl('chapter/page#wrongRefId'))

        code {
            docStructure.validateCollectedLinks()
        } should throwException("can't find a page associated with: chapter/page#wrongRefId\n" +
                "check file: /home/user/docs/chapter/page.md, section title: section title\n")
    }
}
