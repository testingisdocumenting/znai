package com.twosigma.documentation.openapi

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal


class OpenApiIncludePluginTest {
    @Test
    void "should automatically create a section for summary"() {
        def elements = process('open-api-spec.json {operationId: "addPet", autoSection: true}')

        actual(elements[0]).should equal([id: 'addPet', type: 'Anchor'])

        elements[1].title.should == 'Add Pet'
        elements[1].id.should == 'add-pet'
        elements[1].type.should == 'Section'
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-open-api: $params")
        return result*.toMap()
    }
}
