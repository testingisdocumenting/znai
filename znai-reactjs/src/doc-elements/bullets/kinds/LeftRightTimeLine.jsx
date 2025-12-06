/*
 * Copyright 2025 znai maintainers
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
import SvgWithCalculatedSize from './SvgWithCalculatedSize'

import {extractTextLinesEmphasisOrFull, extractTextLines} from '../bulletUtils'
import {isAllAtOnce} from '../../meta/meta'

const stepSize = 15

const Bullet = ({idx}) => {
    const id = "bullet" + idx
    const begin = (0.5 + idx * 0.2) + "s"
    const y = 5 + idx * stepSize

    return (<g className="left-right-timeline">
        <circle id={id} cx="0" cy={y} r="0" fill="#ffb800" stroke="#d89b00" strokeWidth="0.5"/>
        <animate xlinkHref={"#" + id}
                 attributeName="r" from="0" to="2"
                 begin={begin} dur="1s" fill="freeze"/>
    </g>)
}

class TextMessage extends React.Component {
    render() {
        const {idx, text, isRight} = this.props

        const textId = "text" + idx
        const lineId = "line" + idx

        const lx1 = isRight ? 3.8 : -3.8
        const lx2 = isRight ? 20 : -20
        const tx = isRight ? (lx2 + 4) : (lx2 - 4)
        const y = 5 + idx * stepSize
        const anchor = isRight ? "start" : "end"

        return <g>
            <line id={lineId} x1={lx1} y1={y} x2={lx1} y2={y} strokeWidth={0.3} stroke="#888"/>
            <text id={textId} x={tx} y={y} fontSize={4}
                  fill="#666"
                  opacity={0}
                  alignmentBaseline="middle"
                  textAnchor={anchor}>{text}</text>

            <animate ref={node => this.lineAnimation = node}
                     xlinkHref={"#" + lineId}
                     attributeName="x2"
                     from={lx1}
                     to={lx2}
                     begin="indefinite"
                     fill="freeze"
                     dur="1s"/>

            <animate ref={node => this.textAnimation = node}
                     xlinkHref={"#" + textId}
                     attributeName="opacity"
                     from={0}
                     to={1}
                     fill="freeze"
                     dur="1s"/>
        </g>
    }

    componentDidMount() {
        this.lineAnimation.beginElement()
        this.textAnimation.beginElement()
    }
}

const LeftRightTimeLine = ({content, meta, isPresentation, slideIdx}) => {
    const textLines = isPresentation ? extractTextLinesEmphasisOrFull(content) : extractTextLines(content)
    const lastLineIdx = isAllAtOnce(meta) ? textLines.length - 1: slideIdx
    const textLinesToReveal = isPresentation ? textLines.slice(0, lastLineIdx + 1) : textLines

    const bullets = textLines.map((text, idx) => <Bullet key={idx} idx={idx}/>)
    const messages = textLinesToReveal.map((text, idx) => <TextMessage key={idx} idx={idx} text={text}
                                                                       isRight={idx % 2 === 1}/>)

    const height = (textLines.length - 1) * stepSize + 10

    return (
        <SvgWithCalculatedSize isPresentation={isPresentation} viewBox={`-89 0 178 ${height}`}>
            <rect id="timeline" x="-0.7" y="0" height={0} width="1.4" fill="#ddd" stroke="#ccc" strokeWidth="0.3"/>
            <animate xlinkHref="#timeline"
                     attributeName="height"
                     from="0"
                     to={height}
                     fill="freeze"
                     dur="1s"/>
            {bullets}
            {messages}
        </SvgWithCalculatedSize>
    )
}

export default LeftRightTimeLine
