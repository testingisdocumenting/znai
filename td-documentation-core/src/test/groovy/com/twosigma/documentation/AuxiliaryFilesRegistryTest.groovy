package com.twosigma.documentation

import com.twosigma.documentation.core.AuxiliaryFile
import com.twosigma.documentation.core.AuxiliaryFilesRegistry
import com.twosigma.documentation.structure.TocItem
import org.junit.Test

import java.nio.file.Paths

class AuxiliaryFilesRegistryTest {
    @Test
    void "deployment requirement of a file is never overridden"() {
        def registry = new AuxiliaryFilesRegistry()

        def tocItem = new TocItem("", "")
        def path = Paths.get("thePath")
        def afRequiringDeployment = AuxiliaryFile.runTime(path, path)
        def afNotRequiringDeployment = AuxiliaryFile.builtTime(path)

        def pathAssertions = {
            registry.requiresDeployment(path).should == true
            registry.getAuxiliaryFilesForDeployment().anyMatch { it.path == path }.should == true
        }

        registry.updateFileAssociations(tocItem, afRequiringDeployment)
        pathAssertions()

        registry.updateFileAssociations(tocItem, afNotRequiringDeployment)
        pathAssertions()
    }
}
