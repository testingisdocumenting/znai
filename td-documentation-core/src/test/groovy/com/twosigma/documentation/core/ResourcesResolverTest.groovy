package com.twosigma.documentation.core

import org.junit.Test

import java.nio.file.Paths

class ResourcesResolverTest {
    @Test
    void "auxiliary file should use provided url as deploy path if the actual file is outside of docs file structure"() {
        def resolver = [
                isInsideDoc: { false },
                fullPath: { Paths.get(it) },
                docRootRelativePath: { it }] as ResourcesResolver

        def auxiliaryFile = resolver.runtimeAuxiliaryFile("path/image.png")
        auxiliaryFile.getDeployRelativePath().toString().should == "path/image.png"
    }

    @Test
    void "auxiliary file should use relative to docs path if the actual file is inside of docs file structure"() {
        def resolver = [
                isInsideDoc: { true },
                fullPath: { Paths.get("/doc-root/nested").resolve(it) },
                docRootRelativePath: { Paths.get("/doc-root").relativize(it) }] as ResourcesResolver

        def auxiliaryFile = resolver.runtimeAuxiliaryFile("path/image.png")
        auxiliaryFile.getDeployRelativePath().toString().should == "nested/path/image.png"
    }
}
