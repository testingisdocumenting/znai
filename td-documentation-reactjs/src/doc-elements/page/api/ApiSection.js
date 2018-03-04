import React from 'react'

import './ApiSection.css'

const ApiSection = ({elementsLibrary, id, title, isSelected, content}) => {
    const titleClassName = 'title-block' + (isSelected ? ' selected' : '')

    return (
        <div className="api-section content-block">
            <a name={id} href={'#' + id}>
                <div className={titleClassName}>
                    <CollapseIndicator isSelected={isSelected}/>
                    <div className="title">{title}</div>
                </div>
            </a>


            {isSelected && <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                                       content={content}/>}
        </div>
    )
}

function CollapseIndicator({isSelected}) {
    const className = 'collapse-indicator ' + (isSelected ? 'uncollapsed' : 'collapsed')

    return (
        <div className={className}/>
    )
}

export default ApiSection