import React from 'react'

import './OpenApiSchema.css'
import SchemaAsJsonTree from './SchemaAsJsonTree'
import {schemaToJson} from './schemaToJson'

function OpenApiSchema({schema}) {
    const asJson = schemaToJson(schema)

    return (
        <div className="open-api-schema">
            <SchemaAsJsonTree json={asJson}/>
        </div>
    )
}

export default OpenApiSchema
