import {schemaToApiParameters} from './schemaToApiParameters'
import {checkSchema, customerSchema, idsSchema} from './OpenApiSchema.data.demo'

const expectedCustomerSchemaAsParameters = [
    {name: 'firstName', type: 'string', description: [{"text": "customer's first name", "type": "SimpleText"}]},
    {name: 'id', type: 'integer(int64)', description: [{"text": "customer's unique id", "type": "SimpleText"}]},
    {name: 'lastName', type: 'string', description: [{"text": "customer's last name", "type": "SimpleText"}]},
]

describe('schemaToApiParameters', () => {
    it('array of simple', () => {
        const parameters = schemaToApiParameters(idsSchema)
        expect(parameters).toEqual([{
            name: 'root',
            type: 'array of string'
        }])
    })

    it('flat object', () => {
        const parameters = schemaToApiParameters(customerSchema)
        expect(parameters).toEqual(expectedCustomerSchemaAsParameters)
    })

    it('array of objects', () => {
        const parameters = schemaToApiParameters(customerSchema)
        expect(parameters).toEqual([{
            name: 'customers',
            type: 'array of objects',
            children: expectedCustomerSchemaAsParameters}])
    })

    it('nested object', () => {
        const parameters = schemaToApiParameters(checkSchema)

        expect(parameters).toEqual([
            {name: 'id', 'type': 'string', description: [{text: 'id of a customer', type: 'SimpleText'}]},
            {name: 'confirmationIds', 'type': 'array of string'},
            {name: 'customer', type: 'object', description: [{text: 'Customer object', type: 'SimpleText'}], children: expectedCustomerSchemaAsParameters},
            {name: 'customers', type: 'array of objects', children: expectedCustomerSchemaAsParameters}])
    })
})
