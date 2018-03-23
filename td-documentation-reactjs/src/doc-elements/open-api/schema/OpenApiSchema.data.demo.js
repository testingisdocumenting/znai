const customerSchema = {
    "properties": {
        "firstName": {
            "type": "string",
            "description": "customer's first name"
        },
        "id": {
            "type": "integer",
            "format": "int64",
            "description": "customer's unique id"
        },
        "lastName": {
            "type": "string",
            "description": "customer's last name"
        }
    },
    "title": "Customer",
    "description": "Customer object"
}

const checkSchema = {
    "type": "object",
    "properties": {
        "id": {
            "type": "string",
            "description": "id of a customer"
        },
        "confirmationIds": {
            "type": "array",
            "items": {
                type: "string"
            }
        },
        "customer": customerSchema,
        "customers": {
            "type": "array",
            "items": customerSchema
        }
    },
    "title": "Check"
}

const customerArraySchema = {
    "type": "array",
    "items": customerSchema
}

const customerIdsSchema = {
    "type": "array",
    "items": {
        "type": "string"
    }
}

export {customerSchema, checkSchema, customerArraySchema, customerIdsSchema}
