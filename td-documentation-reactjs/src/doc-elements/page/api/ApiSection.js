import React from 'react'

import './ApiSection.css'

const ApiSection = ({elementsLibrary, id, title, isSelected, content}) => {
    const titleClassName = 'title ' + (isSelected ? ' selected' : '')

    return (
        <div className="api-section content-block">
            <a name={id} href={'#' + id}>
                <div className={titleClassName}>{title}</div>
            </a>


            {isSelected && <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                                       content={content}/>}
        </div>
    )
}

export default ApiSection