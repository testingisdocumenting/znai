import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'
import elementsLibrary from '../DefaultElementsLibrary'

class GraphVizFlowFullScreen extends Component {
    constructor(props) {
        super(props)

        const currentSlide = (typeof props.currentSlide === 'undefined') ?
            0: props.currentSlide

        const fullScreen = false

        this.state = {currentSlide, fullScreen}
        this.keyDownHandler = this.keyDownHandler.bind(this)
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }

    keyDownHandler(e) {
        let currentSlide = this.state.currentSlide

        if (e.key === 'ArrowRight') {
            currentSlide += 1
        }

        if (e.key === 'ArrowLeft') {
            currentSlide -= 1
        }

        if (currentSlide < 0) {
            currentSlide = 0
        }

        if (currentSlide >= this.props.slides.length) {
            currentSlide = this.props.slides.length - 1
        }

        this.setState({currentSlide})
    }

    render() {
        const {diagram, colors, slides} = this.props
        const currentSlide = (this.state.currentSlide >= slides.length) ? slides.length - 1 : this.state.currentSlide
        const currentContent = slides[currentSlide].content

        const tempStyle = {marginLeft: 100}

        return <div>
            <GraphVizSvg diagram={diagram} colors={colors} idsToDisplay={this.idsToDisplay()} />
            <br/>
            <div style={tempStyle}>
                <elementsLibrary.DocElement content={currentContent}/>
            </div>
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

export default GraphVizFlowFullScreen