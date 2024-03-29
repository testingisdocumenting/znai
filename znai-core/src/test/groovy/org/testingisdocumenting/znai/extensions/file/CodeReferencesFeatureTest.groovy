/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.file

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class CodeReferencesFeatureTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Before
    @After
    void init() {
        TEST_COMPONENTS_REGISTRY.docStructure().clear()
    }

    @Test
    void "should read csv references, validate links and add to props as a map"() {
        def structure = TEST_COMPONENTS_REGISTRY.docStructure()
        structure.addValidLink('reference/my-class')
        structure.addValidLink('reference/your-class')

        def codeReferences = createCodeReferencesTrait('{referencesPath: "references/test-references.csv"}')
        def props = new LinkedHashMap()
        codeReferences.updateProps(props)

        props.should == [references: [MyClass  : [pageUrl: 'reference/my-class'],
                                      YourClass: [pageUrl: 'reference/your-class']]]
    }

    @Test
    void "should provide link validation error message"() {
        def codeReferences = createCodeReferencesTrait('{referencesPath: "references/test-references.csv"}')

        code {
            def props = new LinkedHashMap()
            codeReferences.updateProps(props)
        } should throwException("no valid link found in doc.md, reference file name: test-references.csv: reference/my-class")
    }

    @Test
    void "should provide auxiliary files stream"() {
        def codeReferences = createCodeReferencesTrait('{referencesPath: "references/test-references.csv"}')

        codeReferences.auxiliaryFiles().collect { it.path.fileName.toString() }.should == ['test-references.csv']
    }

    @Test
    void "should not update props and should return empty auxiliary stream when no reference path was provided"() {
        def codeReferences = createCodeReferencesTrait('')

        codeReferences.auxiliaryFiles().collect { it }.should == []

        def props = new LinkedHashMap()
        codeReferences.updateProps(props)

        props.should == [:]
    }

    private static CodeReferencesFeature createCodeReferencesTrait(String includeFileParams) {
        return new CodeReferencesFeature(TEST_COMPONENTS_REGISTRY, Paths.get("doc.md"),
                pluginParamsFactory.create("include-file", includeFileParams))
    }
}
