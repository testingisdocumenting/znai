import React, {Component} from 'react'
import './LeftRightTimeLine.css'

const stepSize = 15

const Bullet = ({idx}) => {
    const id = "bullet" + idx
    const begin = (0.5 + idx * 0.2) + "s"
    const y = 5 + idx * stepSize

    return (<g className="left-right-timeline"><circle id={id} cx="0" cy={y} r="0" fill="#ffb800" stroke="#d89b00" strokeWidth="0.5"/>
        <animate xlinkHref={"#" + id}
                 attributeName="r" from="0" to="2"
                 begin={begin} dur="1s" fill="freeze"/></g>)
}

class TextMessage extends Component {
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

const Timeline = ({textLines, textLinesToReveal}) => {
    const bullets = textLines.map((text, idx) => <Bullet key={idx} idx={idx}/>)
    const messages = textLinesToReveal.map((text, idx) => <TextMessage key={idx} idx={idx} text={text} isRight={idx % 2 === 1}/>)

    const height = (textLines.length - 1) * stepSize + 10
    const viewPort = `-89 0 178 ${height}`

    return <div className="left-right-timeline content-block">
        <svg width="100%" height="100%" viewBox={viewPort}>
            <rect id="timeline" x="-0.7" y="0" height={height} width="1.4" fill="#ddd" stroke="#ccc" strokeWidth="0.3"/>
            <animate xlinkHref="#timeline"
                     attributeName="height"
                     from="0"
                     to={height}
                     dur="1s"/>
            {bullets}
            {messages}
        </svg>
    </div>
}

const collectTextRecursively = (content, result) => {
    if (! content) {
        return
    }

    content.forEach(item => {
        if (item.type === "SimpleText") {
            result.push(item.text)
        } else {
            collectTextRecursively(item.content, result)
        }
    })

    return result
}

const extractText = (listItem) => {
    const result = []
    collectTextRecursively(listItem.content, result)

    return result.join(" ")
}

const LeftRightTimeLine = ({content, slideIdx}) => {
    const isPresentation = typeof slideIdx !== 'undefined'
    const textLines = content.map(item => extractText(item))
    const textLinesToReveal = isPresentation ? textLines.slice(0, slideIdx) : textLines
    return <Timeline textLines={textLines} textLinesToReveal={textLinesToReveal}/>
}

export default LeftRightTimeLine
