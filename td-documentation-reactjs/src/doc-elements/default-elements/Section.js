import React from 'react'
import RenderingMeta from '../meta/RenderingMeta'

const Section = ({id, title, ...props}) => {
    return (<div className="section" key={title}>
        <div className="content-block">
            <h1 className="section-title" id={id}>{title}</h1>
        </div>
        <props.elementsLibrary.DocElement {...props} renderingMeta={new RenderingMeta()}/>
    </div>)
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const presentationSectionHandler = {component: PresentationTitle,
    numberOfSlides: () => 1,
    slideInfoProvider: ({title}) => {return {sectionTitle: title}}}

export {Section, presentationSectionHandler}
