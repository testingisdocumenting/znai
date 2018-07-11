import React from 'react'

import OpenApiResponse from './OpenApiResponse'
import OpenApiSubHeader from '../common/OpenApiSubHeader'
import OpenApiSchema from '../schema/OpenApiSchema'
import OpenApiMimeTypes from '../common/OpenApiMimeTypes'

import './OpenApiResponses.css'

function OpenApiResponses({responses = {}, produces, elementsLibrary}) {
    if (responses.length === 0) {
        return null
    }

    const responsesWithSchema = responses.filter(r => r.schema)
    const responsesWithoutSchema = responses.filter(r => ! r.schema)

    return (
        <React.Fragment>
            <ResponsesWithSchema responses={responsesWithSchema} produces={produces} elementsLibrary={elementsLibrary}/>
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

function ResponsesWithSchema({responses, produces, elementsLibrary}){
    return responses.map(r => <ResponseWithSchema key={r.code}
                                                  response={r}
                                                  produces={produces}
                                                  elementsLibrary={elementsLibrary}/>)
}

function ResponseWithSchema({response, produces = [], elementsLibrary}) {
    return (
        <React.Fragment>
            <OpenApiSubHeader title={response.code + ' Response'}
                              description={response.description}
                              elementsLibrary={elementsLibrary}/>
            <OpenApiMimeTypes types={produces}/>

            <OpenApiSchema schema={response.schema} elementsLibrary={elementsLibrary}/>
        </React.Fragment>
    )
}

export default OpenApiResponses
