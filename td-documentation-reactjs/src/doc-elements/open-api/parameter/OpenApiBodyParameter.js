import React from 'react'
import OpenApiSchema from '../schema/OpenApiSchema'
import OpenApiSubHeader from '../common/OpenApiSubHeader'

function OpenApiBodyParameter({parameter, elementsLibrary}) {
    if (! parameter) {
        return null;
    }

    return (
        <React.Fragment>
            <OpenApiSubHeader title="Body parameter"
                              description={parameter.description}
                              elementsLibrary={elementsLibrary}/>

            {parameter.schema && <OpenApiSchema schema={parameter.schema} elementsLibrary={elementsLibrary}/>}
        </React.Fragment>
    )
}

export default OpenApiBodyParameter
