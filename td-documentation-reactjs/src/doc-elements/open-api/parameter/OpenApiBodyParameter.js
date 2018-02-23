import React from 'react'
import OpenApiSchema from '../schema/OpenApiSchema'

function OpenApiBodyParameter({elementsLibrary, parameter}) {
    if (! parameter) {
        return null;
    }

    return (
        <React.Fragment>
            <div className="sub-header">Body parameter</div>
            <div className="description">
                <elementsLibrary.DocElement content={parameter.description} elementsLibrary={elementsLibrary}/>
            </div>
            {parameter.schema && <OpenApiSchema schema={parameter.schema}/>}
        </React.Fragment>
    )
}

export default OpenApiBodyParameter
