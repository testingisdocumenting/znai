package org.testingisdocumenting.znai.website

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*

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
}
