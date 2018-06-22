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
    void "should parse spec from yaml"() {
        def yamlSpec = OpenApiSpec.fromYaml(
                TestComponentsRegistry.INSTANCE.markdownParser(),
                ResourceUtils.textContent("open-api-spec.yaml"))

        def findOneCustomerYaml = yamlSpec.findOperationById('findOneCustomerUsingGET')
        findOneCustomerYaml.toMap().should equal(findOneCustomerYaml.toMap())
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
        findOneCustomer.should == [method: 'get', path: '/customers/{id}', tags: ['customer', 'single']]
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

        actual(findAllCustomers.toMap().parameters).should(equal(expectedParameters))
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
        def op = spec.findOperationByMethodAndPath('get', '/pets')
        op.id.should == 'findPets'
    }

    @Test
    void "should substitute schema ref with an actual schema for parameters"() {
        def addPet = spec.findOperationById('addPet')

        addPet.parameters.size().should == 1
        actual(addPet.parameters[0].schema).should(equal([type: 'object', required: ['name'],
                                                          properties: [name: [type: 'string',
                                                                              description: [[markdown: 'pet name', type: 'TestMarkdown']]],
                                                                       tag: [type: 'string']]]))
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
                                                         properties: [name: [type: 'string',
                                                                             description: [[markdown: 'pet name', type: 'TestMarkdown']]],
                                                                      tag: [type: 'string'],
                                                                      id:[format: 'int64', type: 'integer']]]))
    }
}
