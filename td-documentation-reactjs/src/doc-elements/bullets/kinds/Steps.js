import React, {Component} from 'react'

import {extractTextLines} from '../bulletUtils'
import {splitTextIntoLines} from '../../../utils/strings'

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

class Step extends Component {
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

        const parts = splitTextIntoLines(text, 10)
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
    const textLines = extractTextLines(content)
    const textLinesToReveal = isPresentation ? textLines.slice(0, slideIdx + 1) : textLines

    const dx = (totalWidth - (textLinesToReveal.length * stepAllocatedWidth)) / 2

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
}

export default Steps
