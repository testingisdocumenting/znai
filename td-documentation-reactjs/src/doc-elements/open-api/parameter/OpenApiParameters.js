import React from 'react'
import {elementsLibrary} from '../../DefaultElementsLibrary'

import OpenApiParameter from './OpenApiParameter'

import './OpenApiParameters.css'

function OpenApiParameters({label, parameters}) {
    if (parameters.length === 0) {
        return null
    }

    return (
        <React.Fragment>
            <div className="sub-header">{label}</div>
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
