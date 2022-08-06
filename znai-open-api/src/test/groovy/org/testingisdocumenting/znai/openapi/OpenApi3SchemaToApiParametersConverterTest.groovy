/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.openapi

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PropsUtils
import org.testingisdocumenting.znai.utils.JsonUtils

class OpenApi3SchemaToApiParametersConverterTest {
    @Test
    void "convert object"() {
        def root = new OpenApi3Schema("", "object", "root object")
        root.properties.add(new OpenApi3Schema("name", "string", "user name"))
        root.properties.add(new OpenApi3Schema("phone", "string", "user phone number"))

        def apiParams = convertToMap(root)

        apiParams.should == [
                "parameters": [
                        ["name": "name", "type": [["text": "string", "url": ""]], "anchorId": "testprefix_name", "description": [["text": "user name", "type": "testMarkdown"]]],
                        ["name": "phone", "type": [["text": "string", "url": ""]], "anchorId": "testprefix_phone", "description": [["text": "user phone number", "type": "testMarkdown"]]]]
        ]
    }

    @Test
    void "convert array of object"() {
        def root = new OpenApi3Schema("", "array", "people")
        def person = new OpenApi3Schema("", "object", "person")
        person.properties.add(new OpenApi3Schema("name", "string", "user name"))
        person.properties.add(new OpenApi3Schema("phone", "string", "user phone number"))

        root.items = person

        def apiParams = convertToMap(root)
        apiParams.should == ["parameters": [
                        ["name": "", "type": [ ["text": "array of object", "url": ""] ], "anchorId": "testprefix", "description": [ ["text": "people", "type": "testMarkdown"] ],
                         "children": [
                                 ["name": "name", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_name", "description": [ ["text": "user name", "type": "testMarkdown"] ]],
                                 ["name": "phone", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_phone", "description": [ ["text": "user phone number", "type": "testMarkdown"] ]] ]] ]]
    }

    @Test
    void "convert array of string"() {
        def root = new OpenApi3Schema("", "object", "people")
        def telephoneList = new OpenApi3Schema("telephone", "array", "people")
        def telephone = new OpenApi3Schema("", "string", "telephone")

        root.properties.add(telephoneList)
        telephoneList.items = telephone

        def apiParams = convertToMap(root)
        apiParams.should == [
                "parameters": [
                        ["name": "telephone", "type": [ ["text": "array of string", "url": ""] ], "anchorId": "testprefix_telephone", "description": [ ["text": "people", "type": "testMarkdown"] ]] ]
        ]
    }

    @Test
    void "convert composed"() {
        def root = new OpenApi3Schema("", "oneOf", "composed response")

        def person = new OpenApi3Schema("person", "object", "person object")
        person.properties.add(new OpenApi3Schema("name", "string", "user name"))

        def dog = new OpenApi3Schema("dog", "object", "dog object")
        dog.properties.add(new OpenApi3Schema("name", "string", "pet name"))

        root.properties.add(person)
        root.properties.add(dog)

        def apiParams = convertToMap(root)
        apiParams.should == ["parameters": [
                ["name": "", "type": [ ["text": "oneOf", "url": ""] ], "anchorId": "testprefix", "description": [ ["text": "composed response", "type": "testMarkdown"] ],
                 "children": [
                         ["name": "person", "type": [ ["text": "object", "url": ""] ], "anchorId": "testprefix_person", "description": [ ["text": "person object", "type": "testMarkdown"] ],
                          "children": [
                                  ["name": "name", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_person_name", "description": [ ["text": "user name", "type": "testMarkdown"] ]] ]],
                         ["name": "dog", "type": [ ["text": "object", "url": ""] ], "anchorId": "testprefix_dog", "description": [ ["text": "dog object", "type": "testMarkdown"] ],
                          "children": [
                                  ["name": "name", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_dog_name", "description": [ ["text": "pet name", "type": "testMarkdown"] ]] ]
                        ] ]
                ] ]
        ]
    }

    private static Map<String, ?> convertToMap(OpenApi3Schema root) {
        def apiParameters = new OpenApi3SchemaToApiParametersConverter(new SchemaTestMarkdownParser(), "testprefix", root).convert()
        def asMap = PropsUtils.exerciseSuppliers(apiParameters.toMap()) as Map<String, ?>

        System.out.println(JsonUtils.serializePrettyPrint(asMap))

        return asMap
    }
}
