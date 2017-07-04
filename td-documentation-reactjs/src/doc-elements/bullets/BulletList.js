import React from 'react'
import './BulletList.css'

import DefaultBulletList from './kinds/DefaultBulletList'
import LeftRightTimeLine from './kinds/LeftRightTimeLine'
import Venn from './kinds/Venn'
import RevealBoxes from './kinds/RevealBoxes'

const types = {LeftRightTimeLine, Venn}
const presentationTypes = {RevealBoxes, LeftRightTimeLine, Venn}

const BulletList = (props) => {
    const type = listType(props, 'bulletListType')

    if (type === null || type.length === 0 ) {
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
    const isPresentation = typeof props.slideIdx !== 'undefined'

    return <PresentationBullets isPresentation={isPresentation} {...props}/>
}

const presentationNumberOfSlides = ({content, ...props}) => {
    const type = presentationListType(props)
    return (type === null) ? 1 : content.length
}

function valueByIdWithWarning(dict, type) {
    if (! dict.hasOwnProperty(type)) {
        console.warn("can't find bullets list type: " + type)
        return NoBullets
    }

    return dict[type]
}

function presentationListType(props) {
    return listType(props, 'bulletListType') ||
        listType(props, 'presentationBulletListType')
}

function listType(props, key) {
    return (typeof props.meta === 'undefined') ? null : props.meta[key]
}

const presentationBulletListHandler = {component: PresentationBulletList,
    numberOfSlides: presentationNumberOfSlides}

export {BulletList, presentationBulletListHandler}
