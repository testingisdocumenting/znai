import React from 'react'
import OpenApiOperation from './OpenApiOperation'
import {elementsLibrary} from '../../DefaultElementsLibrary'
import {customerSchema} from '../schema/OpenApiSchema.data.demo'

const exampleDescription = [
    {
        "text": "Use ",
        "type": "SimpleText"
    },
    {
        "code": "include-java-enum-entries",
        "type": "InlinedCode"
    },
    {
        "text": " to enumerate entries of a enum from a file",
        "type": "SimpleText"
    }
]

const getExample = {
    method: 'get',
    path: '/findPetsByStatus',
    summary: 'Finds Pets by status',
    description: exampleDescription,
    parameters: [
        {
            name: 'status',
            "in": 'query',
            description: [
                {
                    "text": "Status values that need to be considered for filter",
                    "type": "SimpleText"
                }
            ],
            required: true,
            type: 'array',
            items: {
                type: 'string',
                enum: ['available', 'pending', 'sold'],
                default: 'available'
            }
        }
    ],
    responses: {
        "200": {
            description: "OK",
            schema: customerSchema

        },
        "401": {
            description: "Unauthorized"
        },
        "403": {
            description: "Forbidden"
        },
        "404": {
            description: "Not Found"
        }
    }


}

export function openApiOperationDemo(registry) {
    registry
        .add('get without query parameters', <OpenApiOperation elementsLibrary={elementsLibrary}
                                                               operation={getExample}/>)
}

