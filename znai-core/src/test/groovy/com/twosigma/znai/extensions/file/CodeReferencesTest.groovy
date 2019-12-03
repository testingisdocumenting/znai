package com.twosigma.znai.extensions.file

import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.Test

class CodeReferencesTest {
    @Test
    void "should read csv references and add to props as a map"() {
        def codeReferences = new CodeReferences(TestComponentsRegistry.INSTANCE,
                new PluginParams("include-file", '{referencesPath: "references/test-references.csv"}'))

        def props = new LinkedHashMap()
        codeReferences.updateProps(props)

        props.should == [references: [MyClass: [pageUrl: '/reference/my-class'],
                                      YourClass: [pageUrl: '/reference/your-class']]]
    }

    @Test
    void "should provide auxiliary files stream"() {
        def codeReferences = new CodeReferences(TestComponentsRegistry.INSTANCE,
                new PluginParams("include-file", '{referencesPath: "references/test-references.csv"}'))

        codeReferences.auxiliaryFiles().collect {it.path.fileName.toString()}.should == ['test-references.csv']
    }

    @Test
    void "should not update props and should return empty auxiliary stream when no reference path was provided"() {
        def codeReferences = new CodeReferences(TestComponentsRegistry.INSTANCE,
                new PluginParams("include-file", ''))

        codeReferences.auxiliaryFiles().collect {it}.should == []

        def props = new LinkedHashMap()
        codeReferences.updateProps(props)

        props.should == [:]
    }
}
