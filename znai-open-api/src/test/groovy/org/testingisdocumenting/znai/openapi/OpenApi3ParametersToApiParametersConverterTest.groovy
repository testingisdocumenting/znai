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

class OpenApi3ParametersToApiParametersConverterTest {
    @Test
    void "convert parameters to api parameters"() {
        def strSchema = new OpenApi3Schema("", "string", "")
        def arraySchema = new OpenApi3Schema("", "array", "")
        arraySchema.items = strSchema

        def parameters = [
            new OpenApi3Parameter(name: "firstName", description: "first name", schema: strSchema),
            new OpenApi3Parameter(name: "lastName", description: "last name", schema: arraySchema)
        ]

        def apiParameters = convertToMap(parameters)
        apiParameters.should == [
                "parameters": [ 
                        ["name": "firstName", "type": [ ["text": "string", "url": ""] ], "anchorId": "testprefix_firstName", "description": [ ["text": "first name", "type": "testMarkdown"] ]],
                        ["name": "lastName", "type": [ ["text": "array of string", "url": ""] ], "anchorId": "testprefix_lastName", "description": [ ["text": "last name", "type": "testMarkdown"] ]] ]
        ]
    }

    private static Map<String, ?> convertToMap(List<OpenApi3Parameter> parameters) {
        def apiParameters = new OpenApi3ParametersToApiParametersConverter(new SchemaTestMarkdownParser(), "testprefix", parameters).convert()
        def asMap = PropsUtils.exerciseSuppliers(apiParameters.toMap()) as Map<String, ?>

        System.out.println(JsonUtils.serializePrettyPrint(asMap))

        return asMap
    }
}
