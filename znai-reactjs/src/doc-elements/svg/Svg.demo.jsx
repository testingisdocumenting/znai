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

import {Svg} from './Svg'
import {simulateState} from 'react-component-viewer'

const [getActualSize, setActualSize] = simulateState(false)
const [isValidSrc, setIsValidSrc] = simulateState(false)

export function svgDemo(registry) {
    registry
        .add('simple', () => <Svg svgSrc="svg.svg"/>)
        .add('error loading', () => <Svg svgSrc="not-found.svg"/>)
        .add('partially revealed with actual size and scale', () => (
            <Svg svgSrc="svg.svg"
                 idsToReveal={["partC"]}
                 scale={0.5}
                 actualSize={true}/>
        ))
        .add('flip actual size for preview mode', () => (
            <div>
                <button onClick={toggleActualSize}>toggle actual size</button>
                <Svg svgSrc="svg.svg"
                     idsToReveal={["partC"]}
                     scale={0.5}
                     actualSize={getActualSize()}/>
            </div>
        ))
        .add('flip src for preview mode', () => (
            <div>
                <button onClick={toggleSrc}>toggle src</button>
                <Svg svgSrc={isValidSrc() ? "svg.svg" : "not-found.svg"}/>
            </div>
        ))
}

function toggleActualSize() {
    setActualSize(!getActualSize())
}

function toggleSrc() {
    setIsValidSrc(!isValidSrc())
}
