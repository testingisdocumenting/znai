import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'

import {expandId} from './gvUtils'

class PresentationGraphVizFlow extends Component {
    render() {
        const {diagram, colors} = this.props

        return <div ref={node => this.node = node}>
            <div className="diagram-area">
                <GraphVizSvg diagram={diagram} colors={colors} idsToDisplay={this.idsToDisplay()} />
            </div>
        </div>
    }

    idsToDisplay() {
        const {slides, slideIdx} = this.props
        const ids = []

        for (let i = 0, len = slides.length; i < Math.min(len, slideIdx + 1); i++) {
            slides[i].ids.forEach((id) => expandId(id).forEach((nid) => ids.push(nid)))
        }

        return ids
    }

    componentDidMount() {
        this.props.onLoad(this.node.offsetWidth, this.node.offsetHeight)
    }
}

export default {component: PresentationGraphVizFlow, numberOfSlides: ({slides}) => slides.length}
