package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.MultipleLocationsResourceResolver
import org.junit.Test

import java.nio.file.Paths
import java.util.stream.Stream

/**
 * @author mykola
 */
class MultipleLocationsResourceResolverTest {
    @Test
    void "resolves against specified list of dirs"() {
        def resolver = new MultipleLocationsResourceResolver(Paths.get("/root"), [Paths.get("src/main/java/com/twosigma/documentation"),
                                                                               Paths.get("src/test/groovy/com/twosigma/documentation")].stream())

        assert resolver.fullPath("core/AuxiliaryFile.java").toString() == 'src/main/java/com/twosigma/documentation/core/AuxiliaryFile.java'
        assert resolver.fullPath("parser/MarkdownParserTest.groovy").toString() == 'src/test/groovy/com/twosigma/documentation/parser/MarkdownParserTest.groovy'
    }

    @Test
    void "confirms if file is inside documentation"() {
        def resolver = new MultipleLocationsResourceResolver(Paths.get("/path/to/docs"), Stream.of())
        assert resolver.isInsideDoc(Paths.get("/path/to/docs/image.png"))
    }
}
