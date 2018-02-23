import React from 'react'

function OpenApiParameter({elementsLibrary, parameter}) {
    return (
        <React.Fragment>
            <div className="name">
                {parameter.name}
                {parameter.required && <span className="required">*</span>}
            </div>
            <div className="type">{typeAsStr(parameter)}</div>
            <div className="description">
                <elementsLibrary.DocElement content={parameter.description} elementsLibrary={elementsLibrary}/>
                <div className="values-description">
                    <AvailableValues parameter={parameter}/>
                    <DefaultValue parameter={parameter}/>
                </div>
            </div>
        </React.Fragment>
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
