import React from 'react'
import './BulletList.css'

const BulletList = ({elementsLibrary, content, tight}) => {
    const className = "content-block" + (tight ? " tight" : "")
    return (<ul className={className}><elementsLibrary.DocElement content={content}/></ul>)
}


const Box = ({elementsLibrary, content}) => {
    return <div className="bullet-box"><elementsLibrary.DocElement content={content}/></div>
}

const EmptyBox = () => {
    return <div className="bullet-box-empty"/>
}

const BulletListPresentation = ({elementsLibrary, content, slideIdx}) => {
    const components = Array(content.length).fill().map((nothing, idx) => idx >= slideIdx ? EmptyBox : Box);

    return <div className="bullet-boxes">{content.map((item, idx) => {
        const Component = components[idx]
        return <Component key={idx}
                          elementsLibrary={elementsLibrary}
                          content={item.content}/>})}</div>
}

const presentationUnorderedListHandler = {component: BulletListPresentation,
    numberOfSlides: ({content}) => 1 + content.length}

export {BulletList, presentationUnorderedListHandler}
