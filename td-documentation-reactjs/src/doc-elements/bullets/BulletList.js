import React from 'react'
import './BulletList.css'

import LeftRightTimeLine from './kinds/LeftRightTimeLine'
import RevealBoxes from './kinds/RevealBoxes'

const DefaultBulletList = ({tight, ...props}) => {
    const className = "content-block" + (tight ? " tight" : "")
    return (<ul className={className}><props.elementsLibrary.DocElement {...props}/></ul>)
}

const types = {LeftRightTimeLine}
const presentationTypes = {RevealBoxes, LeftRightTimeLine}

const BulletList = (props) => {
    const type = listType(props, 'listType')

    if (type === null) {
        return <DefaultBulletList {...props}/>
    }

    const Bullets = valueByIdWithWarning(types, type)
    return <Bullets {...props}/>
}

const NoBullets = () => <div className="content-block">No bullets type found</div>

const PresentationBulletList = (props) => {
    const type = presentationListType(props)

    if (type === null) {
        return <DefaultBulletList {...props}/>
    }

    const PresentationBullets = valueByIdWithWarning(presentationTypes, type)
    return <PresentationBullets {...props}/>
}

const presentationNumberOfSlides = ({content, ...props}) => {
    const type = presentationListType(props)
    return (type === null) ? 1 : (content.length + 1)
}

function valueByIdWithWarning(dict, type) {
    if (! dict.hasOwnProperty(type)) {
        console.warn("can't find bullets list type: " + type)
        return NoBullets
    }

    return dict[type]
}

function presentationListType(props) {
    return listType(props, 'listType') ||
        listType(props, 'presentationListType')
}

function listType(props, key) {
    return (typeof props.renderingMeta === 'undefined') ? null : props.renderingMeta.typeParam('BulletList', key)
}

const presentationBulletListHandler = {component: PresentationBulletList,
    numberOfSlides: presentationNumberOfSlides}

export {BulletList, presentationBulletListHandler}
