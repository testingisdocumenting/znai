const customerSchema = {
    "properties": {
        "firstName": {
            "type": "string",
            "description": [
                {
                    "text": "customer's first name",
                    "type": "SimpleText"
                }
            ]
        },
        "id": {
            "type": "integer",
            "format": "int64",
            "description": [
                {
                    "text": "customer's unique id",
                    "type": "SimpleText"
                }
            ]
        },
        "lastName": {
            "type": "string",
            "description": [
                {
                    "text": "customer's last name",
                    "type": "SimpleText"
                }
            ]
        }
    },
    "title": "Customer",
    "description": [
        {
            "text": "Customer object",
            "type": "SimpleText"
        }
    ]
}

const checkSchema = {
    "type": "object",
    "properties": {
        "id": {
            "type": "string",
            "description":[
                {
                    "text": "id of a customer",
                    "type": "SimpleText"
                }
            ]
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
