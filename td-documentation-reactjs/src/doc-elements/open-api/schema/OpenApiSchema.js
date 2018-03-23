import React from 'react'

import {SchemaAsTable} from './SchemaAsTable'

import './OpenApiSchema.css'

function OpenApiSchema({schema, elementsLibrary}) {
    return (
        <div className="open-api-schema">
            <SchemaAsTable schema={schema} elementsLibrary={elementsLibrary}/>
        </div>
    )
}

export default OpenApiSchema
