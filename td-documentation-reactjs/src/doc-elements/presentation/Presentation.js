import React, {Component} from 'react'

import './Presentation.css'

const defaultScaleRatio = 2

class Presentation extends Component {
    constructor(props) {
        super(props)

        this.state = {currentSlideIdx: 0, scaleRatio: defaultScaleRatio}

        this.keyDownHandler = this.keyDownHandler.bind(this)
        this.onClose = this.onClose.bind(this)
        this.calcRatio = this.calcRatio.bind(this)
    }

    render() {
        const {presentationRegistry} = this.props
        const {currentSlideIdx, scaleRatio} = this.state
        const slideAreaStyle = {transform: 'scale(' + scaleRatio + ')'}

        const component = presentationRegistry.componentToRender(currentSlideIdx, this.calcRatio)
        // const showNextButton = currentSlideIdx >= presentationRegistry.numberOfSlides - 1 TODO

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

    componentWillReceiveProps(props) {
        const {presentationRegistry} = this.props
        if (this.scrollToLastWithinPage && presentationRegistry !== props.presentationRegistry) {
            this.scrollToLastWithinPage = false
            this.setState({currentSlideIdx: props.presentationRegistry.numberOfSlides - 1, scaleRatio: defaultScaleRatio})
        }
    }

    calcRatio(width, height) {
        const widthRatio = window.innerWidth / width
        const heightRatio = window.innerHeight / height

        const scaleRatio = Math.min(widthRatio, heightRatio, 2)
        this.setState({scaleRatio, width, height})
    }

    keyDownHandler(e) {
        const {presentationRegistry, onNextPage, onPrevPage} = this.props
        const {scaleRatio} = this.state
        let {currentSlideIdx} = this.state

        if (e.key === 'ArrowRight') {
            currentSlideIdx += 1
        } else if (e.key === 'ArrowLeft') {
            currentSlideIdx -= 1
        } else if (e.key === 'Escape') {
            this.onClose()
            return
        }

        if (currentSlideIdx < 0) {
            this.scrollToLastWithinPage = true
            onPrevPage()
        } else if (currentSlideIdx >= presentationRegistry.numberOfSlides) {
            this.setState({currentSlideIdx: 0, scaleRatio: defaultScaleRatio})
            onNextPage()
        } else {
            this.setState({currentSlideIdx, scaleRatio})
        }
    }

    onClose() {
        const {onClose} = this.props
        if (onClose) {
            onClose()
        }
    }
}

export default Presentation

