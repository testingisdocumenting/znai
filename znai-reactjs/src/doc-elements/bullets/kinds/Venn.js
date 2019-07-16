import React, {Component} from 'react'
import {extractTextLines} from '../bulletUtils'

import SvgWithCalculatedSize from './SvgWithCalculatedSize'

const fillColors = ["#ffb800", "#4a9625", "#6ca5ff", "#964a25"]
class Circle extends Component {
    render() {
        const {idx, totalNumber, text, isPresentation} = this.props

        const id = "venncircle" + idx

        const r1 = 25
        const r2 = totalNumber > 1 ? 22 : 0

        const startingRad = totalNumber === 3 ? (-Math.PI / 2.0) : -Math.PI
        const stepRad = (2.0 * Math.PI / totalNumber)
        const rad = startingRad + stepRad * idx

        const x = r2 * Math.cos(rad)
        const y = r2 * Math.sin(rad)

        const finalStrokeWidth = 0.5
        const initialStrokeWidth = isPresentation ? 3 : finalStrokeWidth

        const fillColor = fillColors[idx]

        return <g>
            <circle id={id} cx={x} cy={y} r={r1} fill={fillColor} strokeWidth={initialStrokeWidth} stroke="#000"
                    opacity={0.5}/>
            <text x={x} y={y} fontSize={4} textAnchor="middle" alignmentBaseline="central">{text}</text>

            <animate ref={node => this.circleAnimation = node}
                     xlinkHref={"#" + id}
                     attributeName="stroke-width"
                     from={initialStrokeWidth}
                     to={finalStrokeWidth}
                     begin="indefinite"
                     fill="freeze"
                     dur="1s"/>
        </g>
    }

    componentDidMount() {
        if (this.props.isPresentation) {
            this.circleAnimation.beginElement()
        }
    }
}

const Venn = ({content, isPresentation, slideIdx}) => {
    const textLines = extractTextLines(content)
    const textLinesToReveal = isPresentation ? textLines.slice(0, slideIdx + 1) : textLines

    return (
        <SvgWithCalculatedSize isPresentation={isPresentation} viewBox="-89 -50 178 90">
            {textLinesToReveal.map((text, idx) => <Circle isPresentation={isPresentation}
                                                          key={idx} idx={idx}
                                                          totalNumber={textLines.length}
                                                          text={text}/>)}
        </SvgWithCalculatedSize>
    )
}

export default Venn
