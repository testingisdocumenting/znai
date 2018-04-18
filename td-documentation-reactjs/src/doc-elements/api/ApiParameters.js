import React from 'react'
import ApiParameter from './ApiParameter'

import './ApiParameters.css'

export default function ApiParameters({parameters, nested, elementsLibrary}) {
    const isExpanded = parameters.length === 1 && parameters[0].children

    const renderedParameters = parameters.map(p => <ApiParameter key={p.name}
                                                                 name={p.name}
                                                                 type={p.type}
                                                                 isExpanded={isExpanded}
                                                                 children={p.children}
                                                                 description={p.description}
                                                                 elementsLibrary={elementsLibrary}/>)

    const className = 'api-parameters' + (nested ? ' nested' : '')
    return (
        <div className={className}>
            {renderedParameters}
        </div>
    )
}
