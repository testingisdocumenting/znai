package com.twosigma.documentation.parser.sphinx

import com.twosigma.utils.ResourceUtils
import org.junit.Test

/**
 * @author mykola
 */
class DocTreeTocGeneratorTest {
    @Test
    void "generates table of contents from index xml file"() {
        def toc = new DocTreeTocGenerator().generate(ResourceUtils.textContent("test-index.xml"))

        toc.contains("NONAME", "intro", "").should == true
        toc.contains("NONAME", "another-page", "").should == true

        toc.contains("NONAME", "garbage", "").should == false
    }
}
