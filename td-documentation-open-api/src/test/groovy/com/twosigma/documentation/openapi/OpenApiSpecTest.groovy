package com.twosigma.documentation.openapi

import com.twosigma.documentation.parser.TestComponentsRegistry
import com.twosigma.utils.ResourceUtils
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal

class OpenApiSpecTest {
    private static OpenApiSpec spec
    private static OpenApiOperation findOneCustomer
    private static OpenApiOperation findAllCustomers

    @BeforeClass
    static void init() {
        spec = OpenApiSpec.fromJson(
                TestComponentsRegistry.INSTANCE.markdownParser(),
                ResourceUtils.textContent("open-api-spec.json"))

        findOneCustomer = spec.findOperationById('findOneCustomerUsingGET')
        findAllCustomers = spec.findOperationById('findAllCustomerUsingGET')
    }

    @Test
    void "should extract all operations from a spec file"() {
        spec.operations.id.should == [
                'findPets',
                'addPet',
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
        findOneCustomer.should == [method: 'get', path: '/customers/{id}', tags: ['customer']]
    }

    @Test
    void "should parse description as markdown and expose as doc elements"() {
        findOneCustomer.description*.toMap().should == [[markdown: 'find one *customer*', type: 'TestMarkdown']]
    }

    @Test
    void "should parse parameters description as markdown and expose as doc elements"() {
        def expectedParameters = [    "in" | "name" | "required" | "type"   | "description"] {
                                  ____________________________________________________________
                                   "query" | "page" | false      | "string" |  [[markdown: 'page', type: 'TestMarkdown']]
                                   "query" | "size" | false      | "string" |  [[markdown: 'size', type: 'TestMarkdown']]
                                   "query" | "sort" | false      | "string" |  [[markdown: 'sort', type: 'TestMarkdown']] }

        actual(findAllCustomers.toMap().parameters).should(equal(expectedParameters))
    }

    @Test
    void "should list all the response codes and descriptions"() {
        def responses = spec.findOperationById('findOneCustomerUsingGET').responses
        responses.should == ['code' | 'description'] {
                            ________________________
                              '200' | 'OK'
                              '401' | 'Unauthorized'
                              '403' | 'Forbidden'
                              '404' | 'Not Found'
        }
    }

    @Test
    void "should substitute schema ref with an actual schema for parameters"() {
        def addPet = spec.findOperationById('addPet')

        addPet.parameters.size().should == 1
        actual(addPet.parameters[0].schema).should(equal([type: 'object', required: ['name'],
                                                          properties: [name: [type: 'string'], tag: [type: 'string']]]))
    }

    @Test
    void "should substitute schema ref with an actual schema for responses"() {
        def operation = spec.findOperationById('findOneCustomerUsingGET')
        def okResponse = operation.responses.get(0)

        // TODO should == need to be AST transformation, otherwise it is not working for maps
        actual(okResponse.code).should(equal('200'))
        actual(okResponse.schema.properties.firstName).should(equal([type: 'string']))
        actual(okResponse.schema.properties.lastName).should(equal([type: 'string']))
    }

    @Test
    void "should replace allOf with the ready to use schema definition"() {
        def addPet = spec.findOperationById('addPet')

        actual(addPet.responses[0].schema).should(equal([type: 'object',
                                                         properties: [name: [type: 'string'],
                                                                      tag: [type: 'string'],
                                                                      id:[format: 'int64', type: 'integer']]]))
    }
}
