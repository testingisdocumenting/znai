package com.twosigma.documentation.structure

import org.junit.Test

import static com.twosigma.testing.Ddjt.code
import static com.twosigma.testing.Ddjt.throwException

class TocItemTest {
    @Test
    void "should not allow special symbols in file name"() {
        def okTocItem = new TocItem('dir-name', 'file-name')

        shouldThrow('dir-name', 'fileName?')
        shouldThrow('dir-name?', 'fileName')
        shouldThrow('dir!-name#', 'fileName!')
    }

    private static void shouldThrow(String dirName, String fileName) {
        code {
            new TocItem(dirName, fileName)
        } should throwException(IllegalArgumentException)
    }
}
