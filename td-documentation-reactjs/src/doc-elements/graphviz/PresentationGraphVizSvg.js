import React from 'react'
import GraphVizSvg from './GraphVizSvg'
import {isAllAtOnce} from '../meta/meta'

const PresentationGraphVizSvg = ({slideIdx, meta, idsToHighlight, ...props}) => {
    const idsToUse = (!idsToHighlight || isAllAtOnce(meta)) ?
        idsToHighlight:
        idsToHighlight.slice(0, slideIdx)

    return <GraphVizSvg idsToHighlight={idsToUse} {...props}/>
}

function numberOfSlides({data, idsToHighlight, meta}) {
    return (!idsToHighlight || isAllAtOnce(meta)) ?
        1 :
        (idsToHighlight.length + 1)
}

export default {component: PresentationGraphVizSvg, numberOfSlides}
