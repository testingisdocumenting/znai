import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'

class GraphVizFlow extends Component {
    constructor(props) {
        super(props)

        const currentSlide = typeof props.currentSlide === 'undefined' ? 
            0: props.currentSlide

        this.state = {currentSlide}
    }

    render() {
        const {svg, colors} = this.props
        return <div>
                <GraphVizSvg svg={svg} colors={colors} idsToDisplay={this.idsToDisplay()} />
            </div>
    }

    idsToDisplay() {
        const {slides} = this.props
        const current = this.state.currentSlide
        const ids = []

        for (let i = 0, len = slides.length; i < Math.min(len, current + 1); i++) {
            slides[i].ids.forEach((id) => ids.push(id))
        }

        return ids
    }
}

export default GraphVizFlow
