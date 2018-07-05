package com.twosigma.documentation.extensions.api

import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.Test

import static com.twosigma.testing.Ddjt.equal

class ApiParametersJsonParserTest {
    @Test
    void "reads api params json and parses description to create doc elemements"() {
        //language=json
        def apiParameters = ApiParametersJsonParser.parse(TestComponentsRegistry.INSTANCE.markdownParser, """
[
    {
        "name": "param1",
        "type": "String",
        "description": "description 1"
    },
    {
        "name": "param2",
        "type": "Integer",
        "description": "description 2",
        "children": [
            {
                "name": "param21",
                "type": "String",
                "description": "description 21",
                "children": [
                    {
                        "name": "param31",
                        "type": "Boolean",
                        "description": "description 31"
                    }
                ]    
            },
            {
                "name": "param22",
                "type": "Integer",
                "description": "description 22"
            }        
        ]    
    }
]
""")

        apiParameters.toMap().should equal([parameters:[
                [name: 'param1', type: 'String', description: [[markdown: 'description 1', type: 'TestMarkdown']]],
                [name: 'param2', type: 'Integer', description: [[markdown: 'description 2', type: 'TestMarkdown']], children:[
                        [name: 'param21', type: 'String', description:[[markdown: 'description 21', type: 'TestMarkdown']], children:[
                                [name: 'param31', type: 'Boolean', description: [[markdown: 'description 31', type: 'TestMarkdown']]]]],
                        [name: 'param22', type: 'Integer', description: [[markdown: 'description 22', type: 'TestMarkdown']]]]]]])
    }
}
