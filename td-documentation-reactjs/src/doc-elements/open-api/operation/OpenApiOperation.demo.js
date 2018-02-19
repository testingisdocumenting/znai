import React from 'react'
import OpenApiOperation from './OpenApiOperation'
import {elementsLibrary} from '../../DefaultElementsLibrary'

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
    ]

}

export function openApiOperationDemo(registry) {
    registry
        .add('get without query parameters', <OpenApiOperation elementsLibrary={elementsLibrary}
                                                               operation={getExample}/>)
}

