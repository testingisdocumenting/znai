import React, {Component} from 'react'
import classNames from 'classnames'

import {extractTextLinesEmphasisOrFull} from '../bulletUtils'
import {isAllAtOnce} from '../../meta/meta'

import './Grid.css'

class Grid extends Component {
    render() {
        const {meta, content, slideIdx} = this.props
        const textLines = extractTextLinesEmphasisOrFull(content)

        return (
            <div className="grid">{content.map((item, idx) => {
                const className = classNames("cell", {"empty": idx > slideIdx && !isAllAtOnce(meta)})
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
