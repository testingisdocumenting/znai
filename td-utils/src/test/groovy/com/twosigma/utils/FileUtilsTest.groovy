package com.twosigma.utils

import org.junit.Test

/**
 * @author mykola
 */
class FileUtilsTest {
    @Test
    void "should read text content from a file"() {
        def testFile = new File("dummy.txt")
        testFile.deleteOnExit()

        testFile.text = "content of a file"
        assert FileUtils.fileTextContent(testFile.toPath()) == "content of a file"
    }
}
