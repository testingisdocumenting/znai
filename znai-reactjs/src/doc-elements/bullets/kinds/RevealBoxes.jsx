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
