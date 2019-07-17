package com.twosigma.znai.openapi

import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

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

    @Test
    void "should detect extension and use appropriate parser, json or yaml"() {
        def elements = process('open-api-spec.yaml {operationId: "addPet", autoSection: true}')
        elements[0].title.should == 'Add Pet'
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-open-api: $params")
        return result*.toMap()
    }
}
