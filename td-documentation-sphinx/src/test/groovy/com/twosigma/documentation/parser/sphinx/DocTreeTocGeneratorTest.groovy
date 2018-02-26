package com.twosigma.documentation.parser.sphinx

import com.twosigma.utils.ResourceUtils
import org.junit.Test

/**
 * @author mykola
 */
class DocTreeTocGeneratorTest {
    @Test
    void "generates table of contents from index xml file"() {
        def toc = new DocTreeTocGenerator(filesExtension()).generate(ResourceUtils.textContent("test-index.xml"))

        toc.contains("chapter-one", "page-three", "").should == true
        toc.contains("chapter-two", "page-four", "").should == true
        toc.contains("chapter-two", "page-five", "").should == true

        toc.contains("wrong-chapter", "garbage", "").should == false
    }
}
