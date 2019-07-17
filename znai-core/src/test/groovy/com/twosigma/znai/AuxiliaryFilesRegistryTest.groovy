package com.twosigma.znai

import com.twosigma.znai.core.AuxiliaryFile
import com.twosigma.znai.core.AuxiliaryFilesRegistry
import com.twosigma.znai.structure.TocItem
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

    @Test
    void "maintains dependency between tocitem and auxiliary files"() {
        def registry = new AuxiliaryFilesRegistry()

        def tocItemA = new TocItem("chapter-a", "page-one")
        def tocItemB = new TocItem("chapter-b", "page-two")

        registry.updateFileAssociations(tocItemA, AuxiliaryFile.builtTime(Paths.get('/root/a1')))
        registry.updateFileAssociations(tocItemA, AuxiliaryFile.builtTime(Paths.get('/root/a2')))
        registry.updateFileAssociations(tocItemB, AuxiliaryFile.builtTime(Paths.get('/root/b1')))
        registry.updateFileAssociations(tocItemB, AuxiliaryFile.builtTime(Paths.get('/root/b2')))

        def auxiliaryFilesA = registry.auxiliaryFilesByTocItem(new TocItem("chapter-a", "page-one"))
        auxiliaryFilesA.path.size().should == 2
        auxiliaryFilesA.path*.toString().sort().should == ['/root/a1', '/root/a2']

        def auxiliaryFilesB = registry.auxiliaryFilesByTocItem(new TocItem("chapter-b", "page-two"))
        auxiliaryFilesB.path.size().should == 2
        auxiliaryFilesB.path*.toString().sort().should == ['/root/b1', '/root/b2']
    }
}
