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

const idsSchema = {
    "type": "array",
    "items": {
        type: "string"
    }
}

const customersSchema = {
    "type": "array",
    "items": customerSchema
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
        "confirmationIdsLongerTail": idsSchema,
        "customer": customerSchema,
        "customers": customersSchema
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

export {customerSchema, checkSchema, idsSchema, customersSchema, customerArraySchema, customerIdsSchema}
