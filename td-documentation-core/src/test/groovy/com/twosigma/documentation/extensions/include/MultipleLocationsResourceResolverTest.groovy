package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.MultipleLocationsResourceResolver
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class MultipleLocationsResourceResolverTest {
    @Test
    void "resolves against specified list of dirs"() {
        def resolver = new MultipleLocationsResourceResolver([Paths.get("src/main/java/com/twosigma/documentation"),
                                                              Paths.get("src/test/groovy/com/twosigma/documentation")].stream())

        assert resolver.fullPath("core/AuxiliaryFile.java").toString() == 'src/main/java/com/twosigma/documentation/core/AuxiliaryFile.java'
        assert resolver.fullPath("parser/MarkdownParserTest.groovy").toString() == 'src/test/groovy/com/twosigma/documentation/parser/MarkdownParserTest.groovy'
    }
}
