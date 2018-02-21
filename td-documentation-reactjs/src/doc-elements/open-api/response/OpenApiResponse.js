import React from 'react'
import OpenApiSchema from '../schema/OpenApiSchema'

function OpenApiResponse({response}) {
    return (
        <React.Fragment>
            <div className="response-code">{response.code}</div>
            <div className="description-and-schema">
                <div className="description">{response.description}</div>
                {response.schema && <OpenApiSchema schema={response.schema}/>}
            </div>
        </React.Fragment>
    )
}

export default OpenApiResponse
