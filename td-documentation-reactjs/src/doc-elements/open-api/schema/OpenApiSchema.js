import React from 'react'

import {schemaToApiParameters} from './schemaToApiParameters'

import ApiParameters from '../../api/ApiParameters'

function OpenApiSchema({schema, elementsLibrary}) {
    const apiParameters = schemaToApiParameters(schema)

    return (
        <ApiParameters parameters={apiParameters} elementsLibrary={elementsLibrary}/>
    )
}

export default OpenApiSchema
