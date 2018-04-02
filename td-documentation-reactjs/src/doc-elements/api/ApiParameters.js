import React from 'react'
import ApiSingleRowParam from './ApiSingleRowParam'

export default function ApiParameters({parameters, elementsLibrary}) {
    const renderedParameters = parameters.map(p => <ApiSingleRowParam key={p.name}
                                                              name={p.name}
                                                              type={p.type}
                                                              description={p.description}
                                                              elementsLibrary={elementsLibrary}/>)

    return (
        <div className="api-parameters content-block">
            {renderedParameters}
        </div>
    )
}
