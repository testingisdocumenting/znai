import React from 'react'
import './OpenApiOperation.css'
import OpenApiParameter from './OpenApiParameter'
import {elementsLibrary} from '../DefaultElementsLibrary'

function OpenApiOperation({elementsLibrary, path, method, summary, description, parameters}) {
    const queryParameters = parameters.filter(p => p.in === 'query')

    return (
        <div className="open-api-operation">
            <div className="url-and-summary">
                <div className="method">{method}</div>
                <div className="path">{path}</div>
                <div className="summary">{summary}</div>
            </div>
            <div className="description">
                <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary}/>
            </div>
            <Parameters label="Query parameters" parameters={queryParameters}/>
        </div>
    )
}

function Parameters({label, parameters}) {
    if (parameters.length === 0) {
        return null
    }

    return (
        <React.Fragment>
            <div className="parameters-label">{label}</div>
            <div className="parameters">
                {parameters.map(p => <OpenApiParameter key={p.name}
                                                       parameter={p}
                                                       elementsLibrary={elementsLibrary}
                />)}
            </div>
        </React.Fragment>
    )
}

export default OpenApiOperation
