import React from 'react'

import {schemaToApiParameters} from './schemaToApiParameters'

import ApiParameters from '../../api/ApiParameters'

import './OpenApiSchema.css'

function OpenApiSchema({schema, elementsLibrary}) {
    const apiParameters = schemaToApiParameters(schema)

    return (
        <div className="open-api-schema">
            <ApiParameters parameters={apiParameters} elementsLibrary={elementsLibrary}/>
        </div>
    )
}

export default OpenApiSchema
