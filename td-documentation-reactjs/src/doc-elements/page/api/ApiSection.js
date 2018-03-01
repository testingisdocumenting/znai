import React from 'react'

import './ApiSection.css'

const ApiSection = ({elementsLibrary, id, title, isSelected, content, onTitleClick}) => {
    const titleClassName = 'title ' + (isSelected ? ' selected' : '')

    return (
        <div className="api-section content-block">

            <div className={titleClassName} onClick={() => onTitleClick(id)}>
                <a href={'#' + id}>{title}</a>
            </div>

            {isSelected && <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                                       content={content}/>}
        </div>
    )
}

export default ApiSection