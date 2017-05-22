import React from 'react'

const stepSize = 20

const Bullet = ({idx}) => {
    const id = "bullet" + idx
    const begin = (0.5 + idx * 0.2) + "s"
    const y = 5 + idx * stepSize

    return (<g><circle id={id} cx="0" cy={y} r="0" fill="#ffb800" stroke="#d89b00" strokeWidth="0.5"/>
        <animate xlinkHref={"#" + id}
                 attributeName="r" from="0" to="2"
                 begin={begin} dur="1s" fill="freeze"/></g>)
}

const TextMessage = ({idx, text, isRight}) => {
    const textId = "text" + idx
    const lineId = "line" + idx

    const lx1 = isRight ? 3.8 : -3.8
    const lx2 = isRight ? 20 : -20
    const tx = isRight ? (lx2 + 4) : (lx2 - 4)
    const y = 5 + idx * stepSize
    const anchor = isRight ? "start" : "end"

    return <g>
        <line id={lineId} x1={lx1} y1={y} x2={lx2} y2={y} strokeWidth={0.3} stroke="#888"/>
        <text id={textId} x={tx} y={y} fontSize={4}
              fill="#666"
              alignmentBaseline="middle"
              textAnchor={anchor}>{text}</text>

        <animate xlinkHref={"#" + lineId}
                 attributeName="x2"
                 from={lx1}
                 to={lx2}
                 dur="1s"/>

        <animate xlinkHref={"#" + textId}
                 attributeName="opacity"
                 from={0}
                 to={1}
                 dur="1s"/>

    </g>
}

const SvgPresentationDemo = () => {
    const parentStyle = {width: "100vw", height: "100vh"}
    return (
        <div style={parentStyle}>
            <svg width="100%" height="100%" viewBox="-100 0 200 100">
                <rect id="timeline" x="-0.7" y="0" height="100" width="1.4" fill="#ddd" stroke="#ccc" strokeWidth="0.3"/>
                <animate xlinkHref="#timeline"
                         attributeName="height"
                         from="0"
                         to="100"
                         dur="1s"/>

                <Bullet idx="0"/>
                <Bullet idx="1"/>
                <Bullet idx="2"/>
                <Bullet idx="3"/>

                <TextMessage idx="0" text="Testing" isRight={false}/>
                <TextMessage idx="1" text="Design" isRight={true}/>
                <TextMessage idx="2" text="Code Review" isRight={false}/>
                <TextMessage idx="3" text="Documentation" isRight={true}/>
            </svg>
        </div>)
}

export default SvgPresentationDemo
