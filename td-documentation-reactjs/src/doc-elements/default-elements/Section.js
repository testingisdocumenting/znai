import React from 'react'

const Section = ({id, title, ...props}) => {
    const sectionTitle = title ? (
        <div className="content-block">
            <h1 className="section-title" id={id}>{title}</h1>
        </div>
    ) : null

    return (
        <div className="section" key={title}>
            {sectionTitle}
            <props.elementsLibrary.DocElement {...props}/>
        </div>
    )
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const presentationSectionHandler = {component: PresentationTitle,
    numberOfSlides: () => 1,
    slideInfoProvider: ({title}) => {return {sectionTitle: title}}}

export {Section, presentationSectionHandler}
