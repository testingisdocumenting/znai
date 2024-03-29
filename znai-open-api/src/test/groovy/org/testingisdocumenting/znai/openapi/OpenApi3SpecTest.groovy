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
    static OpenApi3Spec helloAppSpec2
    static OpenApi3Spec spec3

    static def petProperties = [
            ["name": "name*", "type": [["text": "string", "url": ""]], "anchorId": "testprefix_name", "description": [["text": "pet name\n\\\n*Example*: `doggie`", "type": "testMarkdown"]]],
            ["name": "status", "type": [["text": "string(enum)", "url": ""]], "anchorId": "testprefix_status", "description": [["text": "pet status in the store\n\\\n*Available Values*: `available`, `pending`, `sold`", "type": "testMarkdown"]]]]

    @BeforeClass
    static void init() {
        helloAppSpec2 = OpenApi3Spec.parse(ResourceUtils.textContent("hello-app-spec2.json"))
        spec3 = OpenApi3Spec.parse(ResourceUtils.textContent("test-openapi3.json"))
    }

    @Test
    void "spec3 by operationId"() {
        def findPet = spec3.findById("findPetsByStatus")
        findPet.summary.should == "Finds Pets by status"
        findPet.description.should == "Multiple status values can be provided with comma separated strings"
        findPet.path.should == "/pet/findByStatus"
        findPet.tags.should == ["pet"]
        findPet.request.should == null
    }

    @Test
    void "spec2 by operation id"() {
        def whoAmI = helloAppSpec2.findById("whoami")
        whoAmI.description.should == "Return the user the request was authenticated as (HTTP REMOTE_USER)."
    }

    @Test
    void "spec2 by relative url basePath is not part of search or resulting url"() {
        def whoAmI = helloAppSpec2.findByMethodAndPath("get", "/api/whoami")
        whoAmI.path.should == "/api/whoami"
        whoAmI.description.should == "Return the user the request was authenticated as (HTTP REMOTE_USER)."
    }

    @Test
    void "spec3 by method and path"() {
        def getUser = spec3.findByMethodAndPath("get", "/user/{username}")
        getUser.summary.should == "Get user by user name"
    }

    @Test
    void "anyof request"() {
        def createUser = spec3.findById("createUser")

        def jsonRequest = createUser.request.content.schemaByMimeType.get("application/json")
        def asMap = schemaAsApiParamsMap(jsonRequest)

        asMap.should == [
                "parameters": [
                        ["name": "", "type": [["text": "oneOf", "url": ""]], "anchorId": "testprefix", "description": [["text": "", "type": "testMarkdown"]],
                         "children": [
                                 ["name": "", "type": [["text": "object", "url": ""]], "anchorId": "testprefix", "description": [["text": "", "type": "testMarkdown"]],
                                  "children": [
                                          ["name": "id", "type": [["text": "integer(int64)", "url": ""]], "anchorId": "testprefix_id", "description": [["text": "*Example*: `10`", "type": "testMarkdown"]]],
                                          ["name": "username", "type": [["text": "string", "url": ""]], "anchorId": "testprefix_username", "description": [["text": "*Example*: `theUser`", "type": "testMarkdown"]]]]],
                                 ["name": "", "type": [["text": "object", "url": ""]], "anchorId": "testprefix", "description": [["text": "", "type": "testMarkdown"]],
                                  "children": petProperties]]
                        ]]
        ]
    }

    @Test
    void "additional properties"() {
        def getInventory = spec3.findById("getInventory")

        def jsonResponse = getInventory.responses.get(0).content.schemaByMimeType.get("application/json")
        def asMap = schemaAsApiParamsMap(jsonResponse)

        asMap.should == ["parameters": [
                ["name": "tempId", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_tempId", "description": [ ["text": "temporary inventory id", "type": "testMarkdown"] ]],
                ["name": "< * >", "type": [ ["text": "object", "url": ""] ], "anchorId": "testprefix", "description": [ ["text": "", "type": "testMarkdown"] ],
                 "children": petProperties] ]
        ]
    }

    @Test
    void "convert response to api parameters"() {
        def petById = spec3.findById("getPetById")
        def response = petById.responses.get(0)

        def jsonResponse = response.content.schemaByMimeType.get("application/json")
        def asMap = schemaAsApiParamsMap(jsonResponse)

        asMap.should == ["parameters": petProperties]
    }

    private static Map<String, ?> schemaAsApiParamsMap(OpenApi3Schema schema) {
        def apiParameters = new OpenApi3SchemaToApiParametersConverter(
                new SchemaTestMarkdownParser(), "testprefix", schema).convert()
        def asMap = PropsUtils.exerciseSuppliers(apiParameters.toMap()) as Map<String, ?>

        System.out.println(JsonUtils.serializePrettyPrint(asMap))

        return asMap
    }
}
