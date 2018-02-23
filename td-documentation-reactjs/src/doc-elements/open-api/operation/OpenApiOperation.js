import React from 'react'
import {elementsLibrary} from '../../DefaultElementsLibrary'
import OpenApiResponses from '../response/OpenApiResponses'
import OpenApiParameters from '../parameter/OpenApiParameters'

import './OpenApiOperation.css'
import OpenApiBodyParameter from '../parameter/OpenApiBodyParameter'

function OpenApiOperation({elementsLibrary, operation}) {
    const pathParameters = operation.parameters.filter(p => p.in === 'path')
    const queryParameters = operation.parameters.filter(p => p.in === 'query')
    const bodyParameter = operation.parameters.find(p => p.in === 'body')

    return (
        <div className="open-api-operation">
            <div className="url-and-summary">
                <div className="method">{operation.method}</div>
                <div className="path">{operation.path}</div>
            </div>
            <div className="description">
                <elementsLibrary.DocElement content={operation.description} elementsLibrary={elementsLibrary}/>
            </div>

            <OpenApiParameters label="Path parameters" parameters={pathParameters}/>
            <OpenApiParameters label="Query parameters" parameters={queryParameters}/>
            <OpenApiBodyParameter parameter={bodyParameter} elementsLibrary={elementsLibrary}/>
            <OpenApiResponses responses={operation.responses}/>
        </div>
    )
}

export default OpenApiOperation
