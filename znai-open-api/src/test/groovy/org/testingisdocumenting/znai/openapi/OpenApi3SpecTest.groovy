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


import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.znai.extensions.PropsUtils
import org.testingisdocumenting.znai.utils.JsonUtils
import org.testingisdocumenting.znai.utils.ResourceUtils

class OpenApi3SpecTest {
    static OpenApi3Spec spec

    @BeforeClass
    static void init() {
        spec = OpenApi3Spec.parse(ResourceUtils.textContent("test-openapi3.json"))
    }

    @Test
    void "spec by operationId"() {
        def findPet = spec.findById("findPetsByStatus")
        findPet.summary.should == "Finds Pets by status"
        findPet.description.should == "Multiple status values can be provided with comma separated strings"
        findPet.path.should == "/pet/findByStatus"
        findPet.tags.should == ["pet"]
        findPet.request.should == null
    }

    @Test
    void "anyof handling"() {
        def createUser = spec.findById("createUser")
        def jsonRequest = createUser.request.content.byMimeType.get("application/json")
        def apiParameters = new OpenApi3SchemaToApiParametersConverter(
                new SchemaTestMarkdownParser(), "testprefix", jsonRequest).convert()

        def asMap = PropsUtils.exerciseSuppliers(apiParameters.toMap()) as Map<String, ?>
        System.out.println(JsonUtils.serializePrettyPrint(asMap))

        asMap.should == [
                "parameters": [
                        ["name": "", "type": [ ["text": "oneOf", "url": ""] ], "anchorId": "testprefix", "description": [ ["text": "", "type": "testMarkdown"] ],
                         "children": [
                                 ["name": "", "type": [ ["text": "object", "url": ""] ], "anchorId": "testprefix", "description": [ ["text": "", "type": "testMarkdown"] ],
                                  "children": [
                                          ["name": "id", "type": [ ["text": "integer(int64)", "url": ""] ], "anchorId": "testprefix_id", "description": [ ["text": "*Example*: `10`", "type": "testMarkdown"] ]],
                                          ["name": "username", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_username", "description": [ ["text": "*Example*: `theUser`", "type": "testMarkdown"] ]] ]],
                                 ["name": "", "type": [ ["text": "object", "url": ""] ], "anchorId": "testprefix", "description": [ ["text": "", "type": "testMarkdown"] ],
                                  "children": [
                                          ["name": "name", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_name", "description": [ ["text": "pet name\n\\\n*Example*: `doggie`", "type": "testMarkdown"] ]],
                                          ["name": "status", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_status", "description": [ ["text": "pet status in the store", "type": "testMarkdown"] ]]]] ]
                        ] ]
        ]
    }

    @Test
    void "convert response to api parameters"() {
        def petById = spec.findById("getPetById")
        def response = petById.responses.get(0)

        def jsonResponse = response.content.byMimeType.get("application/json")

        def apiParameters = new OpenApi3SchemaToApiParametersConverter(
                new SchemaTestMarkdownParser(), "testprefix", jsonResponse).convert()
        def asMap = PropsUtils.exerciseSuppliers(apiParameters.toMap()) as Map<String, ?>

        System.out.println(JsonUtils.serializePrettyPrint(asMap))

        asMap.should == [
                "parameters": [
                        ["name": "name", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_name", "description": [ ["text": "pet name\n\\\n*Example*: `doggie`", "type": "testMarkdown"] ]],
                        ["name": "status", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_status", "description": [ ["text": "pet status in the store", "type": "testMarkdown"] ]] ]
        ]
    }
}
