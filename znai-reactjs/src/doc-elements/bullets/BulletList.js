/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'
import './BulletList.css'

import DefaultBulletList from './kinds/DefaultBulletList'
import LeftRightTimeLine from './kinds/LeftRightTimeLine'
import Venn from './kinds/Venn'
import RevealBoxes from './kinds/RevealBoxes'
import Steps from './kinds/Steps'
import HorizontalStripes from './kinds/HorizontalStripes'
import Grid from './kinds/Grid'

import {isAllAtOnce} from '../meta/meta'

const types = {LeftRightTimeLine, Venn, Steps}
const presentationTypes = {...types, RevealBoxes, HorizontalStripes, Grid}

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
    return (type === null || isAllAtOnce(meta)) ? 1 : content.length
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

    const meta = props.meta
    if (! meta.hasOwnProperty(key)) {
        return null
    }

    if (meta[key] === "") {
        return null
    }

    return meta[key]
}

function slideInfoProvider(props) {
    const type = presentationListType(props)
    if (!type) {
        return {}
    }

    const Bullets = valueByIdWithWarning(presentationTypes, type)

    return {
        isSlideCentered: !Bullets.isPresentationFullScreen,
        isSlidePadded: !Bullets.isPresentationFullScreen,
        isSlideScaled: !Bullets.isPresentationFullScreen,
    }
}

const presentationBulletListHandler = {component: PresentationBulletList,
    numberOfSlides: presentationNumberOfSlides,
    slideInfoProvider: slideInfoProvider}

export {BulletList, presentationBulletListHandler}
