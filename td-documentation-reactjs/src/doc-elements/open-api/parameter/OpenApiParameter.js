import React from 'react'
import ApiSingleRowParam from '../../api/ApiSingleRowParam'

function OpenApiParameter({elementsLibrary, parameter}) {
    const name = parameter.name + (parameter.required ? '*' : '')

    const valuesDescription = (
        <div className="values-description">
            <AvailableValues parameter={parameter}/>
            <DefaultValue parameter={parameter}/>
        </div>
    )

    return (
        <ApiSingleRowParam name={name}
                           type={typeAsStr(parameter)}
                           description={parameter.description}
                           elementsLibrary={elementsLibrary}
                           renderedAdditionalDescription={valuesDescription}/>
    )
}

function AvailableValues({parameter}) {
    if (! parameter.items || ! parameter.items.enum) {
        return null
    }

    return (
        <div className="available-values">
            <div className="available-values-label">Available values:</div>
            <div className="values">{parameter.items.enum.join(', ')}</div>
        </div>
    )
}
function DefaultValue({parameter}) {
    if (! parameter.items || ! parameter.items.default) {
        return null
    }

    return (
        <div className="default-value">
            <div className="default-value-label">Default value:</div>
            <div className="value">{parameter.items.default}</div>
        </div>
    )
}

function typeAsStr(parameter) {
    return parameter.type
}

export default OpenApiParameter
