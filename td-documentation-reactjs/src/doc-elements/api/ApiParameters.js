import React from 'react'
import ApiParameter from './ApiParameter'

import './ApiParameters.css'

export default function ApiParameters({parameters, nestedLevel, elementsLibrary}) {
    const isExpanded = parameters.length === 1 && parameters[0].children

    const renderedParameters = parameters.map(p => <ApiParameter key={p.name}
                                                                 name={p.name}
                                                                 type={p.type}
                                                                 isExpanded={isExpanded}
                                                                 children={p.children}
                                                                 description={p.description}
                                                                 nestedLevel={nestedLevel}
                                                                 elementsLibrary={elementsLibrary}/>)

    const isNested = nestedLevel > 0
    return isNested ? renderedParameters :
        (<div className="api-parameters">
            {renderedParameters}
        </div>
    )
}
