/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai

import org.testingisdocumenting.znai.core.AuxiliaryFile
import org.testingisdocumenting.znai.core.AuxiliaryFilesRegistry
import org.testingisdocumenting.znai.structure.TocItem
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
