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

package com.twosigma.znai.typescript

import com.twosigma.utils.JsonUtils
import org.junit.BeforeClass
import org.junit.Test

class TypeScriptCodeTest {
    static TypeScriptCode code

    @BeforeClass
    static void init() {
        code = new TypeScriptCode(sampleData())
    }

    @Test
    void "should find type by name"() {
        def type = code.findType('Sample')

        type.name.should == 'Sample'
        type.properties.should == ['name'      | 'type'   | 'documentation'] {
                                  _____________________________________________
                                   'firstName' | 'string' | 'name of a sample'
                                   'lastName'  | 'string' | '' }
    }

    @Test
    void "should find function by name"() {
        def function = code.findFunction('demo')

        function.jsxDeclarations.tagName.should == ['Declaration']
        function.jsxDeclarations[0].attributes.should == ['name'      | 'value'  ] {
                                                     ________________________________
                                                     'firstName' | '"placeholder"'
                                                     'lastName'  | '{this.lastName}'}
    }

    private static List<Map<String, ?>> sampleData() {
        return JsonUtils.deserializeAsList('[{\n' +
                '            "name": "Sample",\n' +
                '            "documentation": "top level doc string",\n' +
                '            "kind": "type",\n' +
                '            "type": "typeof Sample",\n' +
                '            "members": [{\n' +
                '                "name": "firstName",\n' +
                '                "type": "string",\n' +
                '                "documentation": "name of a sample",\n' +
                '                "kind": "property"\n' +
                '            }, {"name": "lastName", "type": "string", "documentation": "", "kind": "property"}, {\n' +
                '                "name": "methodA",\n' +
                '                "kind": "method",\n' +
                '                "documentation": "method A <b>description</b> and some",\n' +
                '                "parameters": [{"name": "input", "type": " string", "documentation": "for <i>test</i>"}],\n' +
                '                "body": " {\\n        console.log(\'method a body\');\\n        console.log(\'test22\');\\n\\n        const elementA = <Declaration firstName={this.firstName} lastName={this.lastName}/>;\\n        const elementB = (\\n            <Declaration\\n                firstName=\\"placeholder\\"\\n                lastName={this.lastName}\\n            />\\n        );\\n    }",\n' +
                '                "jsxDeclarations": [{\n' +
                '                    "tagName": "Declaration",\n' +
                '                    "attributes": [{"name": "firstName", "value": "{this.firstName}"}, {\n' +
                '                        "name": "lastName",\n' +
                '                        "value": "{this.lastName}"\n' +
                '                    }]\n' +
                '                }, {\n' +
                '                    "tagName": "Declaration",\n' +
                '                    "attributes": [{"name": "firstName", "value": "\\"placeholder\\""}, {\n' +
                '                        "name": "lastName",\n' +
                '                        "value": "{this.lastName}"\n' +
                '                    }]\n' +
                '                }]\n' +
                '            }]\n' +
                '        }, {\n' +
                '            "name": "demo",\n' +
                '            "kind": "function",\n' +
                '            "documentation": "",\n' +
                '            "parameters": [],\n' +
                '            "body": " {\\n    const elementB = (\\n        <Declaration\\n            firstName=\\"placeholder\\"\\n            lastName={this.lastName}\\n        />)\\n}",\n' +
                '            "jsxDeclarations": [{\n' +
                '                "tagName": "Declaration",\n' +
                '                "attributes": [{"name": "firstName", "value": "\\"placeholder\\""}, {\n' +
                '                    "name": "lastName",\n' +
                '                    "value": "{this.lastName}"\n' +
                '                }]\n' +
                '            }]\n' +
                '        }, {\n' +
                '            "name": "Declaration",\n' +
                '            "kind": "function",\n' +
                '            "documentation": "",\n' +
                '            "parameters": [{"type": ""}],\n' +
                '            "body": " {\\n    return null;\\n}",\n' +
                '            "jsxDeclarations": []\n' +
                '        }]')
    }
}
