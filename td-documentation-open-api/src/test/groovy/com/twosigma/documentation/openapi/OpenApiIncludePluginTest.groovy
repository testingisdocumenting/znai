package com.twosigma.documentation.openapi

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal


class OpenApiIncludePluginTest {
    @Test
    void "should automatically create a section for summary"() {
        def elements = process('open-api-spec.json {operationId: "addPet", autoSection: true}')

        elements[0].title.should == 'Add Pet'
        elements[0].id.should == 'add-pet'
        elements[0].type.should == 'Section'
    }

    @Test
    void "should create multiple entries by tags"() {
        def elements = process('open-api-spec.json {tags: "multiple", autoSection: true}')

        elements.should == ['type'    | 'title'] {
                            _______________________________________________________
                            'Section' | 'find all pets'
                            'Section' | 'find all customers ordered by last name'  }
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-open-api: $params")
        return result*.toMap()
    }
}
