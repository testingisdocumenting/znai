import React from 'react'

function OpenApiResponse({response, elementsLibrary}) {
    return (
        <React.Fragment>
            <div className="response-code">{response.code}</div>
            <div className="description">
                <elementsLibrary.DocElement content={response.description} elementsLibrary={elementsLibrary}/>
            </div>
        </React.Fragment>
    )
}

export default OpenApiResponse
