const customerSchema = {
    "type": "object",
    "properties": {
        "firstName": {
            "type": "string"
        },
        "id": {
            "type": "integer",
            "format": "int64"
        },
        "lastName": {
            "type": "string"
        }
    },
    "title": "Customer"
}

const checkSchema = {
    "type": "object",
    "properties": {
        "id": {
            "type": "string"
        },
        "customer": customerSchema
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
