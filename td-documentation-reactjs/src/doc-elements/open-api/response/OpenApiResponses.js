import React from 'react'
import OpenApiResponse from './OpenApiResponse'

import OpenApiSubHeader from '../common/OpenApiSubHeader'
import OpenApiSchema from '../schema/OpenApiSchema'

import './OpenApiResponses.css'

function OpenApiResponses({responses = {}, elementsLibrary}) {
    if (responses.length === 0) {
        return null
    }

    const responsesWithSchema = responses.filter(r => r.schema)
    const responsesWithoutSchema = responses.filter(r => ! r.schema)

    return (
        <React.Fragment>
            <ResponsesWithSchema responses={responsesWithSchema} elementsLibrary={elementsLibrary}/>
            <ResponsesWithoutSchema responses={responsesWithoutSchema} elementsLibrary={elementsLibrary}/>
        </React.Fragment>
    )
}

function ResponsesWithoutSchema({responses, elementsLibrary}){
    if (! responses.length) {
        return null
    }

    return (
        <React.Fragment>
            <OpenApiSubHeader title="Other Responses"/>

            <div className="open-api-responses">
                {responses.map(r => <OpenApiResponse key={r.code}
                                                     response={r}
                                                     elementsLibrary={elementsLibrary}/>)}
            </div>
        </React.Fragment>
    )
}

function ResponsesWithSchema({responses, elementsLibrary}){
    return responses.map(r => <ResponseWithSchema key={r.code}
                                                  response={r}
                                                  elementsLibrary={elementsLibrary}/>)
}

function ResponseWithSchema({response, elementsLibrary}) {
    return (
        <React.Fragment>
            <OpenApiSubHeader title={response.code + ' Response'}
                              description={response.description}
                              elementsLibrary={elementsLibrary}/>

            <OpenApiSchema schema={response.schema} elementsLibrary={elementsLibrary}/>
        </React.Fragment>
    )
}

export default OpenApiResponses
