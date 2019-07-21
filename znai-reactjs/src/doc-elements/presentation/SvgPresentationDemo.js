/*
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

const fillColors = ["#ffb800", "#4a9625", "#6ca5ff", "#964a25"]
const Circle = ({idx, totalNumber, text}) => {
    const id = "circle" + idx

    const r1 = 25
    const r2 = totalNumber > 1 ? 22 : 0

    const startingRad = totalNumber === 3 ? (-Math.PI / 2.0) : -Math.PI
    const stepRad = (2.0 * Math.PI / totalNumber)
    const rad = startingRad  + stepRad * idx

    const x = r2 * Math.cos(rad)
    const y = r2 * Math.sin(rad)

    const fillColor = fillColors[idx]

    return <g>
        <circle id={id} cx={x} cy={y} r={r1} fill={fillColor} opacity={0.5}/>
        <text x={x} y={y} fontSize={4} textAnchor="middle" alignmentBaseline="central">{text}</text>
    </g>
}

const SvgPresentationDemo = () => {
    const parentStyle = {width: "100vw", height: "100vh"}
    return (
        <div style={parentStyle}>
            <svg width="100%" height="100%" viewBox="-100 -50 200 100">
                <Circle idx="0" totalNumber={3} text="reliability"/>
                <Circle idx="1" totalNumber={3} text="96superability"/>
                <Circle idx="2" totalNumber={3} text="total something"/>
            </svg>
        </div>)
}

export default SvgPresentationDemo
