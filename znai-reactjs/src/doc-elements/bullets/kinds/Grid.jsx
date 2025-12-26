/*
 * Copyright 2023 znai maintainers
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

import {extractTextLinesEmphasisOrFull} from '../bulletUtils.js'
import {isAllAtOnce} from '../../meta/meta'

import './Grid.css'

class Grid extends React.Component {
    render() {
        const {meta, content, slideIdx} = this.props
        const textLines = extractTextLinesEmphasisOrFull(content)

        return (
            <div className="grid">{content.map((item, idx) => {
                const className = "cell" + (idx > slideIdx && !isAllAtOnce(meta) ? " empty" : "")
                return <div key={idx} className={className} style={cellStyle(textLines.length)}>{textLines[idx]}</div>
            })}
            </div>
        )
    }

    static get isPresentationFullScreen() {
        return true
    }
}

function cellStyle(numberOfItems) {
    const {horizontal, vertical} = calcPercents(numberOfItems)

    return {flexGrow: 0, flexShrink: 0, flexBasis: `${horizontal}%`, height: `${vertical}%`}
}

const third = 100/3.0
function calcPercents(numberOfItems) {
    if (numberOfItems <= 4) {
        return {horizontal: 50, vertical: 50}
    }

    if (numberOfItems <= 6) {
        return {horizontal: third, vertical: 50}
    }

    if (numberOfItems <= 9) {
        return {horizontal: third, vertical: third}
    }

    if (numberOfItems <= 12) {
        return {horizontal: 25, vertical: third}
    }

    return {horizontal: 25, vertical: 25}
}

export default Grid
