import React from 'react'
import './BulletList.css'

import DefaultBulletList from './kinds/DefaultBulletList'
import LeftRightTimeLine from './kinds/LeftRightTimeLine'
import Venn from './kinds/Venn'
import RevealBoxes from './kinds/RevealBoxes'
import Steps from './kinds/Steps'

const types = {LeftRightTimeLine, Venn, Steps}
const presentationTypes = {RevealBoxes, LeftRightTimeLine, Venn, Steps}

const BulletList = (props) => {
    const type = listType(props, 'bulletListType')

    if (! type) {
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

const presentationNumberOfSlides = (props) => {
    const {content, meta} = props

    const type = presentationListType(props)
    return (type === null || meta.allAtOnce) ? 1 : content.length
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
    if (! props.hasOwnProperty('meta')) {
        return null
    }

    return props.meta[key]
}

const presentationBulletListHandler = {component: PresentationBulletList,
    numberOfSlides: presentationNumberOfSlides}

export {BulletList, presentationBulletListHandler}
