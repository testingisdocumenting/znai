import React from 'react'
import OpenApiResponse from './OpenApiResponse'

import './OpenApiResponses.css'

function OpenApiResponses({responses = {}}) {
    if (responses.length === 0) {
        return null
    }

    return (
        <React.Fragment>
            <div className="sub-header">Responses</div>
            <div className="open-api-responses">
                {responses.map(r => <OpenApiResponse key={r.code}
                                                     response={r}/>)}
            </div>
        </React.Fragment>
    )
}

export default OpenApiResponses
