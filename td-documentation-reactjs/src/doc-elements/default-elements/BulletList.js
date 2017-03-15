import React from 'react'

const BulletList = ({elementsLibrary, content, tight}) => {
    const className = "content-block" + (tight ? " tight" : "")
    return (<ul className={className}><elementsLibrary.DocElement content={content}/></ul>)
}

const presentationUnorderedListHandler = {component: BulletList,
    numberOfSlides: () => 1}

export {BulletList, presentationUnorderedListHandler}
