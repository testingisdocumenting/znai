import React from 'react'
import OpenApiSchema from './OpenApiSchema'

import {elementsLibrary} from '../../DefaultElementsLibrary'

import {checkSchema, customerArraySchema, customerIdsSchema, customerSchema} from './OpenApiSchema.data.demo'

export function openApiSchemaDemo(registry) {
    registry
        .add('array of strings', <OpenApiSchema schema={customerIdsSchema} elementsLibrary={elementsLibrary}/>)
        .add('array of objects', <OpenApiSchema schema={customerArraySchema} elementsLibrary={elementsLibrary}/>)
        .add('flat object', <OpenApiSchema schema={customerSchema} elementsLibrary={elementsLibrary}/>)
        .add('nested object', <OpenApiSchema schema={checkSchema} elementsLibrary={elementsLibrary}/>)
}

