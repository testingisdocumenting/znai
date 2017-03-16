import React, {Component} from 'react'

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const Section = ({elementsLibrary, id, title, content}) => {
    return (<div className="section" key={title}>
        <div className="content-block">
            <div className="section-title" id={id}>{title}</div>
        </div>
        <elementsLibrary.DocElement content={content}/>
    </div>)
}

const presentationSectionHandler = {component: PresentationTitle,
    numberOfSlides: () => 1}

export {Section, presentationSectionHandler}
