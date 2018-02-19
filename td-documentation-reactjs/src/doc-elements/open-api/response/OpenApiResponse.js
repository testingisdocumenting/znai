import React from 'react'
import OpenApiSchema from '../schema/OpenApiSchema'

function OpenApiResponse({code, response}) {
    return (
        <React.Fragment>
            <div className="response-code">{code}</div>
            <div className="description-and-schema">
                <div className="description">{response.description}</div>
                {response.schema && <OpenApiSchema schema={response.schema}/>}
            </div>
        </React.Fragment>
    )
}

export default OpenApiResponse
