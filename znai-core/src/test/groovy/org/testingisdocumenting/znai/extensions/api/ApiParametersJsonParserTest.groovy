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

package com.twosigma.znai.extensions.api

import org.junit.Test

import static com.twosigma.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class ApiParametersJsonParserTest {
    @Test
    void "reads api params json and parses description to create doc elemements"() {
        //language=json
        def apiParameters = ApiParametersJsonParser.parse(TEST_COMPONENTS_REGISTRY.markdownParser(), """
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

        apiParameters.toMap().should == [parameters:[
                [name: 'param1', type: 'String', description: [[markdown: 'description 1', type: 'TestMarkdown']]],
                [name: 'param2', type: 'Integer', description: [[markdown: 'description 2', type: 'TestMarkdown']], children:[
                        [name: 'param21', type: 'String', description:[[markdown: 'description 21', type: 'TestMarkdown']], children:[
                                [name: 'param31', type: 'Boolean', description: [[markdown: 'description 31', type: 'TestMarkdown']]]]],
                        [name: 'param22', type: 'Integer', description: [[markdown: 'description 22', type: 'TestMarkdown']]]]]]]
    }
}
