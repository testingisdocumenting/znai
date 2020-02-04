package com.twosigma.znai.extensions.file

import com.twosigma.znai.extensions.PluginParams
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.Matchers.code
import static com.twosigma.webtau.Matchers.throwException
import static com.twosigma.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class CodeReferencesFeatureTest {
    @Before
    @After
    void init() {
        TEST_COMPONENTS_REGISTRY.docStructure().clearValidLinks()
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
                new PluginParams("include-file", includeFileParams))
    }
}
