package org.testingisdocumenting.znai.website

import org.testingisdocumenting.znai.core.DocMeta
import org.testingisdocumenting.znai.html.Deployer
import org.testingisdocumenting.znai.structure.TableOfContents
import org.testingisdocumenting.znai.website.markups.MarkdownParsingConfiguration
import org.junit.Test

import java.nio.file.Files

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PageRedirectsTest {
    @Test
    void "parse redirects"() {
        def result = PageRedirects.parse("""# optional comment
old-chapter/old-page,new-chapter/new-page
old-chapter/old-page-two,top-level-new-page
""")
        result.should == [                 "oldLink" |  "newDirName" | "newFileNameWithoutExtension"] {
                        __________________________________________________________________________
                              "old-chapter/old-page" | "new-chapter" | "new-page"
                          "old-chapter/old-page-two" | ""            | "top-level-new-page" }
    }

    @Test
    void "validation checks"() {
        code {
            PageRedirects.parse("""# optional comment
old-chapter/old-page,new-chapter/new-page/sub-page
""") } should throwException("invalid url format, expected [dirName/]fileName")
    }

    @Test
    void "redirect stub target ends with trailing slash"() {
        def docStructure = createDocStructure("product")
        def html = PageRedirects.redirectPageHtml(docStructure,
                docStructure.tableOfContents().findTocItem("chapter", "pageOne"))

        html.should contain('url=/product/chapter/pageOne/"')
    }

    @Test
    void "redirect stub target starts with single slash when doc id is empty"() {
        def docStructure = createDocStructure("")
        def html = PageRedirects.redirectPageHtml(docStructure,
                docStructure.tableOfContents().findTocItem("chapter", "pageOne"))

        html.should contain('url=/chapter/pageOne/"')
    }

    @Test
    void "redirect stub target for top level page has no double slashes"() {
        def docStructure = createDocStructure("product")
        def html = PageRedirects.redirectPageHtml(docStructure,
                docStructure.tableOfContents().findTocItem("", "top-page"))

        html.should contain('url=/product/top-page/"')
    }

    @Test
    void "redirect stub target for index page points at doc root"() {
        def docStructure = createDocStructure("product")
        PageRedirects.redirectPageHtml(docStructure, docStructure.tableOfContents().index)
                .should contain('url=/product"')

        def rootDocStructure = createDocStructure("")
        PageRedirects.redirectPageHtml(rootDocStructure, rootDocStructure.tableOfContents().index)
                .should contain('url=/"')
    }

    @Test
    void "deploys redirect stub for renamed page"() {
        def docStructure = createDocStructure("product")

        def tempDir = Files.createTempDirectory("znai-page-redirects")
        def csvPath = tempDir.resolve("redirects.csv")
        Files.writeString(csvPath, "old-chapter/old-page,chapter/pageOne\n")

        def deployer = new Deployer(tempDir, tempDir.resolve("deploy"))
        new PageRedirects(docStructure, deployer, csvPath).deployRedirectPages()

        Files.readString(tempDir.resolve("deploy/old-chapter/old-page/index.html"))
                .should contain('url=/product/chapter/pageOne/"')
    }

    private static WebSiteDocStructure createDocStructure(String docId) {
        def docMeta = new DocMeta([:])
        docMeta.setId(docId)

        def toc = new TableOfContents("md")
        toc.addTocItem("chapter", "pageOne")
        toc.addTocItem("", "top-page")
        toc.addIndex()

        return new WebSiteDocStructure(TEST_COMPONENTS_REGISTRY, docMeta, toc, new MarkdownParsingConfiguration())
    }
}
