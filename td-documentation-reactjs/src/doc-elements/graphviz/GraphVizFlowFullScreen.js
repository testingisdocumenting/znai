import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'
import elementsLibrary from '../DefaultElementsLibrary'

import {expandId} from './gvUtils'

class GraphVizFlowFullScreen extends Component {
    constructor(props) {
        super(props)

        const currentSlideIdx = (typeof props.currentSlideIdx === 'undefined') ?
            0: props.currentSlideIdx

        const fullScreen = false

        this.state = {currentSlideIdx, fullScreen}
        this.keyDownHandler = this.keyDownHandler.bind(this)
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }

    keyDownHandler(e) {
        let currentSlideIdx = this.state.currentSlideIdx

        if (e.key === 'ArrowRight') {
            currentSlideIdx += 1
        }

        if (e.key === 'ArrowLeft') {
            currentSlideIdx -= 1
        }

        if (currentSlideIdx < 0) {
            currentSlideIdx = 0
        }

        if (currentSlideIdx >= this.props.slides.length) {
            currentSlideIdx = this.props.slides.length - 1
        }

        this.setState({currentSlideIdx})
    }

    render() {
        const {diagram, colors, slides, onClose} = this.props
        const currentSlideIdx = (this.state.currentSlideIdx >= slides.length) ? slides.length - 1 : this.state.currentSlideIdx
        const currentContent = slides[currentSlideIdx].content

        return <div className="graphviz-diagram-full-screen-container">
            <div className="overlay"></div>
            <div className="close" onClick={onClose}>&times;</div>
            <div className="graphviz-diagram-full-screen">
                <div className="slide-number">
                    {currentSlideIdx + 1}/{slides.length}
                </div>
                <div className="diagram-area">
                    <GraphVizSvg diagram={diagram} colors={colors} idsToDisplay={this.idsToDisplay()} />
                </div>
                <div className="explanation-area">
                    <elementsLibrary.DocElement content={currentContent}/>
                </div>
            </div>
        </div>
    }

    idsToDisplay() {
        const {slides} = this.props
        const current = this.state.currentSlideIdx
        const ids = []

        for (let i = 0, len = slides.length; i < Math.min(len, current + 1); i++) {
            slides[i].ids.forEach((id) => expandId(id).forEach((nid) => ids.push(nid)))
        }

        return ids
    }
}

export default GraphVizFlowFullScreen