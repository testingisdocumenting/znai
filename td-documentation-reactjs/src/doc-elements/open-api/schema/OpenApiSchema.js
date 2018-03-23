import React from 'react'

import {SchemaAsTable} from './SchemaAsTable'

import './OpenApiSchema.css'

function OpenApiSchema({schema}) {
    return (
        <div className="open-api-schema">
            <SchemaAsTable schema={schema}/>
        </div>
    )
}

export default OpenApiSchema
