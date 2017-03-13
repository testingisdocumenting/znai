import React, {Component} from 'react'

import {presentationRegistry} from './PresentationRegistry'

import './Presentation.css'

class Presentation extends Component {
    constructor(props) {
        super(props)

        this.state = {currentSlideIdx: 0, scaleRatio: 2}

        this.keyDownHandler = this.keyDownHandler.bind(this)
        this.onClose = this.onClose.bind(this)
        this.calcRatio = this.calcRatio.bind(this)
    }

    render() {
        const {currentSlideIdx, scaleRatio} = this.state
        const slideAreaStyle = {transform: 'scale(' + scaleRatio + ')'}

        const component = presentationRegistry.componentToRender(currentSlideIdx, this.calcRatio)
        return (<div className="presentation">
            <div className="presentation-close-icon glyphicon glyphicon-remove" onClick={this.onClose}/>
            <div className="slide-number">
                {currentSlideIdx + 1}/{presentationRegistry.numberOfSlides}
            </div>
            <div className="slide-area" style={slideAreaStyle}>
                {component}
            </div>
        </div>)
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }

    calcRatio(width, height) {
        console.log("calcRatio", width, height)

        const widthRatio = window.innerWidth / width
        const heightRatio = window.innerHeight / height

        const scaleRatio = Math.min(widthRatio, heightRatio, 2)
        this.setState({scaleRatio, width, height})
    }

    keyDownHandler(e) {
        let {currentSlideIdx} = this.state

        if (e.key === 'ArrowRight') {
            currentSlideIdx += 1
        } else if (e.key === 'ArrowLeft') {
            currentSlideIdx -= 1
        } else if (e.key === 'Escape') {
            this.onClose()
        }

        if (currentSlideIdx < 0) {
            currentSlideIdx = 0
        }

        if (currentSlideIdx >= presentationRegistry.numberOfSlides) {
            currentSlideIdx = presentationRegistry.numberOfSlides - 1
        }

        const scaleRatio = 2 // TODO calc for everything, not just images
        this.setState({currentSlideIdx, scaleRatio})
    }

    onClose() {
        const {onClose} = this.props
        if (onClose) {
            onClose()
        }
    }
}

export default Presentation

