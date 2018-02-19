import {schemaToJson} from './schemaToJson'
import {checkSchema, customerArraySchema, customerIdsSchema, customerSchema} from './OpenApiSchema.data.demo'

const expectedCustomerSchemaAsJson = {
    "firstName": {
        "type": "string"
    },
    "id": {
        "type": "integer",
        "format": "int64"
    },
    "lastName": {
        "type": "string"
    }}

describe('schemaToJson', () => {
    it('flat object', () => {
        const asJson = schemaToJson(customerSchema)
        expect(asJson).toEqual(expectedCustomerSchemaAsJson)
    })

    it('nested object', () => {
        const asJson = schemaToJson(checkSchema)
        expect(asJson).toEqual({
            "id": {"type": "string"},
            "customer": expectedCustomerSchemaAsJson})
    })

    it('array of strings', () => {
        const asJson = schemaToJson(customerIdsSchema)
        expect(asJson).toEqual([{type: "string"}])
    })

    it('array of flat object', () => {
        const asJson = schemaToJson(customerArraySchema)
        expect(asJson).toEqual([expectedCustomerSchemaAsJson])
    })
})