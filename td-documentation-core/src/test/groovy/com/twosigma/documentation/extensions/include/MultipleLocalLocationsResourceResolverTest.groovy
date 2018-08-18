package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.MultipleLocalLocationsResourceResolver
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class MultipleLocalLocationsResourceResolverTest {
    @Test
    void "resolves against specified list of dirs"() {
        def resolver = new MultipleLocalLocationsResourceResolver(Paths.get(""))
        resolver.initialize(["src/main/java/com/twosigma/documentation",
                             "src/test/groovy/com/twosigma/documentation"].stream())

        assert resolver.fullPath("core/AuxiliaryFile.java").toString() == 'src/main/java/com/twosigma/documentation/core/AuxiliaryFile.java'
        assert resolver.fullPath("parser/MarkdownParserTest.groovy").toString() == 'src/test/groovy/com/twosigma/documentation/parser/MarkdownParserTest.groovy'
    }

    @Test
    void "confirms if file is inside documentation"() {
        def resolver = new MultipleLocalLocationsResourceResolver(Paths.get("/path/to/docs"))
        assert resolver.isInsideDoc(Paths.get("/path/to/docs/image.png"))
    }
}
