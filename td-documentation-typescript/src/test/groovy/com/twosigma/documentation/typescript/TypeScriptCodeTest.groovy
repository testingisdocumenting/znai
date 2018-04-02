package com.twosigma.documentation.typescript

import com.twosigma.utils.JsonUtils
import org.junit.Test

class TypeScriptCodeTest {
    @Test
    void "should find type by name"() {
        def code = new TypeScriptCode(JsonUtils.deserializeAsList('[{"name":"Sample","documentation":"top level doc string","type":"typeof Sample","members":[{"name":"firstName","type":"string","documentation":"name of a sample","kind":"property"},{"name":"lastName","type":"string","documentation":"","kind":"property"},{"name":"methodA","kind":"method","documentation":"method A <b>description</b> and some","parameters":[{"name":"input","type":" string","documentation":"for <i>test</i>"}],"body":" {\\n        console.log(\'method a body\');\\n        console.log(\'test22\');\\n    }"}]}]'))
        def type = code.findType('Sample')

        type.name.should == 'Sample'
        type.properties.should == ['name'      | 'type'   | 'documentation'] {
                                  _____________________________________________
                                   'firstName' | 'string' | 'name of a sample'
                                   'lastName'  | 'string' | '' }
    }
}
