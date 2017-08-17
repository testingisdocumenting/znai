import React, {Component} from 'react'

import {extractTextLinesEmphasisOrFull} from '../bulletUtils'
import {isAllAtOnce} from '../../meta/meta'

import './HorizontalStripes.css'

const Stripe = ({text}) => {
    return <div className="horizontal-stripe">{text}</div>
}

const EmptyStripe = () => {
    return <div className="empty-horizontal-stripe"/>
}

class HorizontalStripes extends Component {
    render() {
        const {elementsLibrary, meta, content, slideIdx} = this.props
        const textLines = extractTextLinesEmphasisOrFull(content)

        return (
            <div className="horizontal-stripes">{content.map((item, idx) => {
                const Component = idx <= slideIdx || isAllAtOnce(meta) ? Stripe : EmptyStripe
                return <Component key={idx}
                                  {...this.props}
                                  elementsLibrary={elementsLibrary}
                                  text={textLines[idx]}/>
            })}
            </div>
        )
    }

    static get isPresentationFullScreen() {
        return true
    }
}

export default HorizontalStripes
