import React from 'react'

import {extractTextLinesEmphasisOrFull} from '../bulletUtils'
import {isAllAtOnce} from '../../meta/meta'

import './RevealBoxes.css'

const Box = ({text}) => {
    return <div className="bullet-box">{text}</div>
}

const EmptyBox = () => {
    return <div className="bullet-box-empty"/>
}

const RevealBoxes = ({elementsLibrary, meta, content, slideIdx, ...props}) => {
    const components = Array(content.length).fill()
        .map((nothing, idx) => (idx <= slideIdx || isAllAtOnce(meta)) ? Box : EmptyBox)

    const textLines = extractTextLinesEmphasisOrFull(content)

    return (
        <div className="bullet-boxes">{content.map((item, idx) => {
            const Component = components[idx]
            return <Component key={idx}
                              {...props}
                              elementsLibrary={elementsLibrary}
                              text={textLines[idx]}/>
        })}
        </div>
    )
}

export default RevealBoxes
