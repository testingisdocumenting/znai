import React from 'react'
import {elementsLibrary} from '../../DefaultElementsLibrary'
import OpenApiResponses from '../response/OpenApiResponses'
import OpenApiParameters from '../parameter/OpenApiParameters'

import './OpenApiOperation.css'

function OpenApiOperation({elementsLibrary, operation}) {
    const queryParameters = operation.parameters.filter(p => p.in === 'query')

    return (
        <div className="open-api-operation">
            <div className="url-and-summary">
                <div className="method">{operation.method}</div>
                <div className="path">{operation.path}</div>
                <div className="summary">{operation.summary}</div>
            </div>
            <div className="description">
                <elementsLibrary.DocElement content={operation.description} elementsLibrary={elementsLibrary}/>
            </div>

            <OpenApiParameters label="Query parameters" parameters={queryParameters}/>
            <OpenApiResponses responses={operation.responses}/>
        </div>
    )
}

export default OpenApiOperation
