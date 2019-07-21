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

const allParameters = [
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
    },
    {
        "name": "id",
        "in": "path",
        "description": [
            {
                "text": "ID of pet to delete",
                "type": "SimpleText"
            }
        ],
        "required": true,
        "type": "integer",
        "format": "int64"
    },
    {
        "name": "tags",
        "in": "query",
        "description": [
            {
                "text": "tags to filter by",
                "type": "SimpleText"
            }
        ],
        "required": false,
        "type": "array",
        "collectionFormat": "csv",
        "items": {
            "type": "string"
        }
    },
    {
        "name": "file",
        "in": "formData",
        "required": true,
        "type": "file",
        "description": []
    },
    {
        "name": "saveAs",
        "in": "formData",
        "required": false,
        "type": "string",
        "description": []
    },
    {
        "name": "limit",
        "in": "query",
        "description": [
            {
                "text": "maximum number of results to return",
                "type": "SimpleText"
            }
        ],
        "required": false,
        "type": "integer",
        "format": "int32"
    },
    {
        "name": "limit",
        "in": "body",
        "description": [
            {
                "text": "Pet to add to the store",
                "type": "SimpleText"
            }
        ],
        "required": false,
        "schema": {
            "type": "object",
            "required": [
                "name"
            ],
            "properties": {
                "name": {
                    "type": "string"
                },
                "tag": {
                    "type": "string"
                }
            }
        },
    }
]

const responses = [
    {
        code: "Default",
        description: [
            {
                "text": "OK",
                "type": "SimpleText"
            }
        ],
        schema: customerSchema
    },
    {
        code: "401",
        description: [
            {
                "text": "Unauthorized",
                "type": "SimpleText"
            }
        ]
    },
    {
        code: "403",
        description: [
            {
                "text": "Forbidden",
                "type": "SimpleText"
            }
        ]
    },
    {
        code: "404",
        description: [
            {
                "text": "Not Found",
                "type": "SimpleText"
            }
        ]
    }
]

const getExampleCoreFields = {
    method: 'get',
    path: '/findPetsByStatus',
    summary: 'Finds Pets by status',
    consumes: ['application/json'],
    produces: ['application/json', 'application/xml'],
    description: exampleDescription
}

const getExample = {
    ...getExampleCoreFields,
    parameters: allParameters,
    responses: responses
}

const getExampleWithoutParams = {
    ...getExampleCoreFields,
    responses: responses
}

export function openApiOperationDemo(registry) {
    registry
        .add('GET request with all type of parameters', () => <OpenApiOperation elementsLibrary={elementsLibrary}
                                                                                operation={getExample}/>)
        .add('GET request with consumes produces info', () => <OpenApiOperation elementsLibrary={elementsLibrary}
                                                                                operation={getExample}
                                                                                showConsumes={true}
                                                                                showProduces={true}/>)
        .add('GET request without parameters', () => <OpenApiOperation elementsLibrary={elementsLibrary}
                                                                       operation={getExampleWithoutParams}/>)
}

