import React from 'react'
import OpenApiSchema from './OpenApiSchema'

import {checkSchema, customerArraySchema, customerIdsSchema, customerSchema} from './OpenApiSchema.data.demo'

export function openApiSchemaDemo(registry) {
    registry
        .add('flat object', <OpenApiSchema schema={customerSchema}/>)
        .add('nested object', <OpenApiSchema schema={checkSchema}/>)
        .add('array of strings', <OpenApiSchema schema={customerIdsSchema}/>)
        .add('array of objects', <OpenApiSchema schema={customerArraySchema}/>)
}
