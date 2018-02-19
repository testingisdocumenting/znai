import React from 'react'
import './OpenApiParameter.css'

function OpenApiParameter({elementsLibrary, parameter}) {
    return (
        <div className="open-api-parameter">
            <div className="name-and-type">
                <div className="name">{parameter.name}</div>
                <IsRequired parameter={parameter}/>
                <div className="type">{typeAsStr(parameter)}</div>
            </div>
            <div className="description">
                <elementsLibrary.DocElement content={parameter.description} elementsLibrary={elementsLibrary}/>
                <div className="values-description">
                    <AvailableValues parameter={parameter}/>
                    <DefaultValue parameter={parameter}/>
                </div>
            </div>
        </div>
    )
}

function IsRequired({parameter}) {
    if (! parameter.required) {
        return null
    }

    return (
        <div className="required">required</div>
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
