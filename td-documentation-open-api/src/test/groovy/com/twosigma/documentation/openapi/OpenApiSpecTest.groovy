package com.twosigma.documentation.openapi

import com.twosigma.utils.ResourceUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.actual
import static com.twosigma.testing.Ddjt.equal

class OpenApiSpecTest {
    private static OpenApiSpec spec = OpenApiSpec.fromJson(ResourceUtils.textContent("open-api-spec.json"))

    @Test
    void "should extract all operations from a spec file"() {
        spec.operations.id.should == [
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
        def operation = spec.findOperationById('findOneCustomerUsingGET')
        operation.should == [method: 'get', path: '/customers/{id}']
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
    void "should substitute schema ref with an actual schema for responses"() {
        def operation = spec.findOperationById('findOneCustomerUsingGET')
        def okResponse = operation.responses.get(0)

        // TODO should == need to be AST transformation, otherwise it is not working for maps
        actual(okResponse.code).should(equal('200'))
        actual(okResponse.schema.properties.firstName).should(equal([type: 'string']))
        actual(okResponse.schema.properties.lastName).should(equal([type: 'string']))
    }
}
