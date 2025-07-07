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

import {extractDefinitions} from './definitionsExtractor.js';

describe('definitionsExtractor', () => {
    it('parses js docs', () => {
        const definitions = extractDefinitions('src/test/resources/Sample.tsx');

        console.log(JSON.stringify(definitions))

        expect(definitions).toEqual([{
                "name": "Sample",
                "documentation": "top level doc string",
                "type": "typeof Sample",
                "members": [{
                    "name": "firstName",
                    "type": "string",
                    "documentation": "name of a sample",
                    "kind": "property"
                }, {"name": "lastName", "type": "string", "documentation": "", "kind": "property"}, {
                    "name": "methodA",
                    "kind": "method",
                    "documentation": "method A <b>description</b> and some",
                    "parameters": [{"name": "input", "type": " string", "documentation": "for <i>test</i>"}],
                    "body": " {\n        console.log('method a body');\n        console.log('test22');\n\n        const elementA = <Declaration firstName={this.firstName} lastName={this.lastName}/>;\n        const elementB = (\n            <Declaration\n                firstName=\"placeholder\"\n                lastName={this.lastName}\n            />\n        );\n    }",
                    "jsxDeclarations": [{
                        "tagName": "Declaration",
                        "attributes": [{"name": "firstName", "value": "{this.firstName}"}, {
                            "name": "lastName",
                            "value": "{this.lastName}"
                        }]
                    }, {
                        "tagName": "Declaration",
                        "attributes": [{"name": "firstName", "value": "\"placeholder\""}, {
                            "name": "lastName",
                            "value": "{this.lastName}"
                        }]
                    }]
                }],
                "kind": "type"
            }, {
                "name": "Props",
                "documentation": "",
                "type": "any",
                "members": [{
                    "name": "firstName",
                    "type": "string",
                    "documentation": "first name",
                    "kind": "property"
                }, {"name": "lastName", "type": "string", "documentation": "last name", "kind": "property"}],
                "kind": "type"
            }, {
                "name": "demo",
                "kind": "function",
                "documentation": "",
                "parameters": [],
                "body": " {\n    const elementB = (\n        <Declaration\n            firstName=\"placeholder\"\n            lastName={this.lastName}\n        />)\n}",
                "jsxDeclarations": [{
                    "tagName": "Declaration",
                    "attributes": [{"name": "firstName", "value": "\"placeholder\""}, {
                        "name": "lastName",
                        "value": "{this.lastName}"
                    }]
                }]
            }, {
                "name": "Declaration",
                "kind": "function",
                "documentation": "",
                "parameters": [{"type": ""}],
                "body": " {\n    return null;\n}",
                "jsxDeclarations": []
            }]
        )

    });
});