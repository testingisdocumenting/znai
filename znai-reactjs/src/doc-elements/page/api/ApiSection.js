import React from 'react'

import './ApiSection.css'

const ApiSection = ({elementsLibrary, id, title, isSelected, height, content}) => {
    const titleClassName = 'title-block' + (isSelected ? ' selected' : '')

    return (
        <React.Fragment>
            <a name={id} href={'#' + (isSelected ? '' : id)}>
                <div className={titleClassName}>
                    <CollapseIndicator isSelected={isSelected}/>
                    <div className="title">{title}</div>
                </div>
            </a>

            <div className="api-section-content" style={heightStyle()}>
                <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                            content={content}/>
            </div>
        </React.Fragment>
    )

    function heightStyle() {
        if (! height) {
            return {}
        }

        return isSelected ?
            {maxHeight: height}:
            {maxHeight: 0}
    }
}

function CollapseIndicator({isSelected}) {
    const className = 'collapse-indicator ' + (isSelected ? 'uncollapsed' : 'collapsed')

    return (
        <div className={className}/>
    )
}

export default ApiSection