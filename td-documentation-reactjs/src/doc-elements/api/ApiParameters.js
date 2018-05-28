import React from 'react'
import ApiParameter from './ApiParameter'

import './ApiParameters.css'

export default function ApiParameters({parameters, nestedLevel, parentWidth = 0, elementsLibrary}) {
    const renderedParameters = parameters.map(p => <ApiParameter key={p.name}
                                                                 name={p.name}
                                                                 type={p.type}
                                                                 isExpanded={false}
                                                                 children={p.children}
                                                                 description={p.description}
                                                                 nestedLevel={nestedLevel}
                                                                 elementsLibrary={elementsLibrary}/>)

    const isNested = nestedLevel > 0
    const className = 'api-parameters' + (isNested ? ' nested' : '')
    const style = {marginLeft: -parentWidth}

    return (
        <div className={className} style={style}>
            {renderedParameters}
        </div>
    )
}
