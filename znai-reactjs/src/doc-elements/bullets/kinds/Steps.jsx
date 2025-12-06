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

import {extractTextLines, extractTextLinesEmphasisOrFull} from '../bulletUtils'
import {splitTextIntoLinesUsingThreshold} from '../../../utils/strings'
import {isAllAtOnce} from '../../meta/meta'

import colors from './colors'

import SvgWithCalculatedSize from './SvgWithCalculatedSize'

const baseWidth = 95
const noseLength = 20
const stepWidth = baseWidth + noseLength
const height = 65
const halfHeight = height / 2
const halfWidth = stepWidth / 2
const topBottomPadding = 10

const totalWidth = 650

const fontSize = 10
const lineHeight = 1.2
const fontHeight = fontSize * lineHeight
const halfFontHeight = fontHeight / 2.0

const distanceBetweenSteps = 2
const stepAllocatedWidth = (stepWidth + distanceBetweenSteps)

class Step extends React.Component {
    render() {
        const {idx, color} = this.props

        const id = "step" + idx

        const sx = (stepAllocatedWidth) * idx
        const sy = 0

        const shapeStyle = {stokeWidth: "1px", stroke: color.line, fill: color.fill}

        return (
            <g id={id} transform={`translate(${sx}, ${sy})`}>
                <path d={`M ${noseLength} ${halfHeight} l -${noseLength} -${halfHeight} l ${baseWidth} 0
                    l ${noseLength} ${halfHeight} l -${noseLength} ${halfHeight} l -${baseWidth} 0 z`}
                      style={shapeStyle}/>
                {this.renderText()}
            </g>
        )
    }

    renderText() {
        const {text,color} = this.props

        const textStyle = {fill: color.text}

        const parts = splitTextIntoLinesUsingThreshold(text, 10)
        const y = parts.length === 1 ? halfHeight :
            (halfHeight - (fontHeight * parts.length / 2.0) + halfFontHeight)

        return parts.map((part, idx) => (
            <text key={idx}
                  x={halfWidth}
                  y={y + idx * fontHeight} fontSize={fontSize} textAnchor="middle"
                  style={textStyle}
                  alignmentBaseline="central">
                {part}
            </text>)
        )
    }
}

const Steps = ({content, meta, isPresentation, slideIdx}) => {
    const textLines = isPresentation ? extractTextLinesEmphasisOrFull(content) : extractTextLines(content)
    const lastLineIdx = isAllAtOnce(meta) ? textLines.length - 1: slideIdx
    const textLinesToReveal = isPresentation ? textLines.slice(0, lastLineIdx + 1) : textLines

    const dx = calcDx()

    return (
        <SvgWithCalculatedSize isPresentation={isPresentation}
                               viewBox={`-${distanceBetweenSteps} -${topBottomPadding} ${totalWidth} ${height + 2 * topBottomPadding}`}>
            <g transform={`translate(${dx}, ${0})`}>
                {textLinesToReveal.map((text, idx) => {
                    const color = meta.differentColors === true ? colors[idx % colors.length] : colors[0]
                    return (
                        <Step isPresentation={isPresentation}
                              key={idx} idx={idx}
                              color={color}
                              text={text}/>)
                })}
            </g>
        </SvgWithCalculatedSize>
    )

    function calcDx() {
        const align = meta.align || "center"
        const stepsWidth = (textLinesToReveal.length * stepAllocatedWidth)

        if (align === "left") {
            return 0;
        }

        if (align === "right") {
            return totalWidth - stepsWidth;
        }

        return (totalWidth - stepsWidth) / 2;
    }
}

export default Steps
