import React from 'react'

import OpenApiParameter from './OpenApiParameter'
import OpenApiSubHeader from '../common/OpenApiSubHeader'

import './OpenApiParameters.css'

function OpenApiParameters({label, parameters, elementsLibrary}) {
    if (parameters.length === 0) {
        return null
    }

    return (
        <React.Fragment>
            <OpenApiSubHeader title={label}/>
            <div className="open-api-parameters">
                {parameters.map(p => <OpenApiParameter key={p.name}
                                                       parameter={p}
                                                       elementsLibrary={elementsLibrary}
                />)}
            </div>
        </React.Fragment>
    )
}

export default OpenApiParameters
