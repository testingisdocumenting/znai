import React from 'react'

import OpenApiSubHeader from '../common/OpenApiSubHeader'

import {openApiParameterToApiParameter} from './openApiParameterToApiParameter'
import ApiParameters from '../../api/ApiParameters'

import './OpenApiParameters.css'

function OpenApiParameters({label, parameters, elementsLibrary}) {
    if (parameters.length === 0) {
        return null
    }

    const apiParameters = parameters.map(p => openApiParameterToApiParameter(p))

    return (
        <React.Fragment>
            <OpenApiSubHeader title={label}/>
            <div className="open-api-parameters">
                <ApiParameters parameters={apiParameters} elementsLibrary={elementsLibrary}/>
            </div>
        </React.Fragment>
    )
}

export default OpenApiParameters
