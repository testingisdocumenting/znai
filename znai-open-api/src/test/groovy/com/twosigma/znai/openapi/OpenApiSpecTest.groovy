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

package com.twosigma.znai.openapi

import com.twosigma.znai.utils.ResourceUtils
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.BeforeClass
import org.junit.Test

class OpenApiSpecTest {
    private static OpenApiSpec spec
    private static OpenApiOperation findOneCustomer
    private static OpenApiOperation findAllCustomers
    private static OpenApiOperation addPet

    @BeforeClass
    static void init() {
        spec = OpenApiSpec.fromJson(
                TestComponentsRegistry.INSTANCE.markdownParser(),
                ResourceUtils.textContent("open-api-spec.json"))

        findOneCustomer = spec.findOperationById('findOneCustomerUsingGET')
        findAllCustomers = spec.findOperationById('findAllCustomerUsingGET')
        addPet = spec.findOperationById('addPet')
    }

    @Test
    void "should parse spec from yaml"() {
        def yamlSpec = OpenApiSpec.fromYaml(
                TestComponentsRegistry.INSTANCE.markdownParser(),
                ResourceUtils.textContent("open-api-spec.yaml"))

        def findOneCustomerYaml = yamlSpec.findOperationById('findOneCustomerUsingGET')
        findOneCustomerYaml.toMap().should == findOneCustomerYaml.toMap()
    }

    @Test
    void "should extract all operations from a spec file"() {
        spec.operations.id.should == [
                'findPets',
                'addPet',
                'dummyOp',
                'findAllCustomerUsingGET',
                'saveCustomerUsingPOST',
                'findAllByOrderByLastNameCustomerUsingGET',
                'findOneCustomerUsingGET',
                'saveCustomerUsingPUT',
                'deleteCustomerUsingDELETE',
                'saveCustomerUsingPATCH']
    }

    @Test
    void "operation should consist of method, path and tag"() {
        findOneCustomer.should == [method: 'get', path: '/api/customers/{id}', tags: ['customer', 'single']]
    }

    @Test
    void "operation should expose produces and consumes types"() {
        addPet.should == [consumes: ['application/binary'], produces: ['application/pdf', 'application/json']]
    }

    @Test
    void "spec global produces and consumes should be used when none is specified"() {
        findOneCustomer.should == [consumes: ['application/xml'], produces: ['application/json']]
    }

    @Test
    void "should parse description as markdown and expose as doc elements"() {
        findOneCustomer.description.should == [[markdown: 'find one *customer*', type: 'TestMarkdown']]
    }

    @Test
    void "should parse parameters description as markdown and expose as doc elements"() {
        def expectedParameters = [    "in" | "name" | "required" | "type"   | "description"] {
                                  ____________________________________________________________
                                   "query" | "page" | false      | "string" |  [[markdown: 'page', type: 'TestMarkdown']]
                                   "query" | "size" | false      | "string" |  [[markdown: 'size', type: 'TestMarkdown']]
                                   "query" | "sort" | false      | "string" |  [[markdown: 'sort', type: 'TestMarkdown']] }

        findAllCustomers.toMap().parameters.should == expectedParameters
    }

    @Test
    void "should list all the response codes and descriptions"() {
        def responses = spec.findOperationById('findOneCustomerUsingGET').responses
        responses.should == ['code' | 'description'] {
                            __________________________________________________________
                              '200' | [[markdown: 'OK', type: 'TestMarkdown']]
                              '401' | [[markdown: 'Unauthorized', type: 'TestMarkdown']]
                              '403' | [[markdown: 'Forbidden', type: 'TestMarkdown']]
                              '404' | [[markdown: 'Not Found', type: 'TestMarkdown']]
        }
    }

    @Test
    void "should find operations by tags"() {
        def multiple = spec.findOperationsByTags(['multiple'])
        multiple.id.should == ['findPets', 'findAllByOrderByLastNameCustomerUsingGET']

        def multiplePets = spec.findOperationsByTags(['multiple', 'pet'])
        multiplePets.id.should == ['findPets']
    }

    @Test
    void "should find operation by method and path"() {
        def op = spec.findOperationByMethodAndPath('get', '/api/pets')
        op.id.should == 'findPets'
    }

    @Test
    void "should substitute schema ref with an actual schema for parameters"() {
        def addPet = spec.findOperationById('addPet')

        addPet.parameters.size().should == 1
        addPet.parameters[0].schema.should == [type: 'object', required: ['name'],
                                                          properties: [name: [type: 'string',
                                                                              description: [[markdown: 'pet name', type: 'TestMarkdown']]],
                                                                       tag: [type: 'string']]]
    }

    @Test
    void "should substitute schema ref with an actual schema for responses"() {
        def operation = spec.findOperationById('findOneCustomerUsingGET')
        def okResponse = operation.responses.get(0)

        okResponse.code.should == '200'
        okResponse.schema.properties.firstName.should == [type: 'string']
        okResponse.schema.properties.lastName.should == [type: 'string']
    }

    @Test
    void "should replace allOf with the ready to use schema definition"() {
        def addPet = spec.findOperationById('addPet')

        addPet.responses[0].schema.should == [type: 'object',
                                                         properties: [name: [type: 'string',
                                                                             description: [[markdown: 'pet name', type: 'TestMarkdown']]],
                                                                      tag: [type: 'string'],
                                                                      id:[format: 'int64', type: 'integer']]]
    }
}
