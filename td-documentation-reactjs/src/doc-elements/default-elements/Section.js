import React from 'react'
import RenderingMeta from '../meta/RenderingMeta'

const Section = ({id, title, ...props}) => {
    return (<div className="section" key={title}>
        <div className="content-block">
            <div className="section-title" id={id}>{title}</div>
        </div>
        <props.elementsLibrary.DocElement {...props} renderingMeta={new RenderingMeta()}/>
    </div>)
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const presentationSectionHandler = {component: PresentationTitle,
    numberOfSlides: () => 1}

export {Section, presentationSectionHandler}
