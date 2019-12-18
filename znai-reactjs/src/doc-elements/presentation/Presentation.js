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

import React, {Component} from 'react'

import './Presentation.css'

const defaultScaleRatio = 3

class Presentation extends Component {
    constructor(props) {
        super(props)

        this.state = {currentSlideIdx: this.props.slideIdx || 0, scaleRatio: defaultScaleRatio}
    }

    render() {
        const {docMeta, presentationRegistry} = this.props
        const {currentSlideIdx, isAppeared, scaleRatio} = this.state
        const slideContent = presentationRegistry.renderedComponent(currentSlideIdx)

        const {pageTitle, sectionTitle} = presentationRegistry.extractCombinedSlideInfo(currentSlideIdx - 1)

        const slide = presentationRegistry.slideByIdx(currentSlideIdx)
        const isSectionTitleOnSlide = !! slide.info.sectionTitle

        const slideVisibleNote = slide.info.slideVisibleNote
        const showSlideNote = typeof slideVisibleNote !== "undefined" && slideVisibleNote !== null
        const slideNoteClass = "presentation-footer" + ((showSlideNote && slideVisibleNote.length === 0) ? " size-only" : "")

        const slideClassName = "slide-area" + (isAppeared ?  " appeared": "") +
            (slide.info.isFullScreen ? " full-screen" : "")

        const slideAreaStyle = slide.info.isFullScreen ? {display: "flex", flex: 1} : {transform: "scale(" + scaleRatio + ")"}

        return (
            <div className="presentation" onClick={this.onMouseClick}>
                <div className="header">
                    <div className="product-info">
                        <div className="znai-documentation-logo"/>
                        <div className="product-title">{docMeta.title}</div>
                    </div>

                    <div className="slide-info">
                        <div className="presentation-page-title">{pageTitle}</div>
                        {pageTitle && (! isSectionTitleOnSlide) ?
                            <span className="presentation-title-divider">&gt;&gt;</span> :
                            null
                        }
                        <div className="presentation-section-title">{isSectionTitleOnSlide ? null : sectionTitle}</div>
                    </div>

                    <div className="controls">
                        <div className="slide-number">
                            {currentSlideIdx + 1}/{presentationRegistry.numberOfSlides}
                        </div>

                        <div className="close">
                            <div className="glyphicon glyphicon-remove" onClick={this.onClose}/>
                        </div>
                    </div>
                </div>

                <div className={slideClassName} ref={(n) => this.slideAreaDom = n}>
                    <div ref={(n) => this.componentDom = n} style={slideAreaStyle}>
                        {slideContent}
                    </div>
                </div>

                {showSlideNote ? <div className={slideNoteClass}>{slideVisibleNote}</div> : null }
            </div>
        )
    }

    componentDidMount() {
        document.addEventListener("keydown", this.keyDownHandler)
        this.updateSlide()
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.currentSlideIdx !== this.state.currentSlideIdx) {
            this.updateSlide()
        }
    }

    componentWillUnmount() {
        document.removeEventListener("keydown", this.keyDownHandler)
    }

    componentWillReceiveProps(props) {
        const {presentationRegistry} = this.props
        if (this.scrollToLastWithinPage && presentationRegistry !== props.presentationRegistry) {
            this.scrollToLastWithinPage = false
            this.setState({currentSlideIdx: props.presentationRegistry.numberOfSlides - 1, scaleRatio: defaultScaleRatio})
        }
    }

    updateSlide() {
        this.updateScaleRatio()
        this.updateAnimation()
    }

    updateScaleRatio() {
        const {presentationRegistry} = this.props
        const {currentSlideIdx} = this.state

        const slide = presentationRegistry.slideByIdx(currentSlideIdx)
        const hPad = slide.info.isFullScreen ? 0 : 60
        const vPad = slide.info.isFullScreen ? 0 : 30

        const width = this.componentDom.offsetWidth
        const height = this.componentDom.offsetHeight

        const widthRatio = (this.slideAreaDom.offsetWidth - hPad) / width
        const heightRatio = (this.slideAreaDom.offsetHeight - vPad) / height

        const scaleRatio = Math.min(widthRatio, heightRatio, defaultScaleRatio)

        this.setState({scaleRatio})
    }

    updateAnimation() {
        setTimeout(() => {
            this.setState({isAppeared: true})
        }, 0)
    }

    keyDownHandler = (e) => {
        if (e.code === "ArrowRight" || e.code === "PageDown" || e.code === "Space") {
            this.incrementSlide();
        } else if (e.code === "ArrowLeft" || e.code === "PageUp") {
            this.decrementSlide();
        }
    }

    onMouseClick = () => {
        this.incrementSlide()
    }

    onClose = () => {
        const {onClose} = this.props
        if (onClose) {
            onClose()
        }
    }

    decrementSlide() {
        const {onPrevPage} = this.props
        const {currentSlideIdx} = this.state
        const newSlideIdx = currentSlideIdx - 1

        if (newSlideIdx < 0) {
            this.scrollToLastWithinPage = true
            onPrevPage()
        } else {
            this.setState({currentSlideIdx: newSlideIdx, scaleRatio: defaultScaleRatio, isAppeared: false})
        }

    }

    incrementSlide() {
        const {presentationRegistry, onNextPage} = this.props
        const {currentSlideIdx} = this.state
        const newSlideIdx = currentSlideIdx + 1

        if (newSlideIdx >= presentationRegistry.numberOfSlides) {
            this.setState({currentSlideIdx: 0, scaleRatio: defaultScaleRatio, isAppeared: false})
            onNextPage()
        } else {
            this.setState({currentSlideIdx: newSlideIdx, scaleRatio: defaultScaleRatio, isAppeared: false})
        }
    }
}

export default Presentation
