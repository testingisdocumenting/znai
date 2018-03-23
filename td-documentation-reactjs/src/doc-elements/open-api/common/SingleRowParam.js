import React from 'react'

import './SingleRowParam.css'

export default function SingleRowParam({
                                           name,
                                           type,
                                           description,
                                           isExpanded,
                                           actionElements,
                                           renderedAdditionalDescription,
                                           elementsLibrary
                                       }) {
    const className = 'open-api-single-row-param' + (isExpanded ? ' expanded' : '')

    return (
        <div className={className}>
            <div className="name-and-type">
                <div className="name">{name}</div>
                <div className="type">{type}</div>
                {actionElements}
            </div>
            <div className="description">
                <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary}/>
                {renderedAdditionalDescription}
            </div>
        </div>
    )
}
