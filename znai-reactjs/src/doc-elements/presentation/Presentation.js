/*
 * Copyright 2020 znai maintainers
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

import { Icon } from '../icons/Icon'

import {SlidesLayout} from "./SlidesLayout";

import './Presentation.css'

const maxScaleRatio = 3

class Presentation extends Component {
    constructor(props) {
        super(props)

        this.state = {
            currentSlideIdx: this.props.slideIdx || 0
        }
    }

    render() {
        const {docMeta, presentationRegistry} = this.props
        const {currentSlideIdx} = this.state

        const {pageTitle, sectionTitle} = presentationRegistry.extractCombinedSlideInfo(currentSlideIdx - 1)

        const slide = presentationRegistry.slideByIdx(currentSlideIdx)
        const isSectionTitleOnSlide = !!slide.info.sectionTitle

        const slideVisibleNote = slide.info.slideVisibleNote
        const showSlideNote = !!slideVisibleNote

        return (
            <div className="presentation" onClick={this.onMouseClick}>
                <div className="header">
                    <div className="product-info">
                        <div className="znai-documentation-logo"/>
                        <div className="product-title">{docMeta.title}</div>
                    </div>

                    <div className="slide-info">
                        <div className="presentation-page-title">{pageTitle}</div>
                        {pageTitle && !isSectionTitleOnSlide && sectionTitle.length !== 0 ?
                            <span className="presentation-title-divider">&gt;&gt;</span> :
                            null
                        }
                        <div className="presentation-section-title">{isSectionTitleOnSlide ? null : sectionTitle}</div>
                    </div>

                    <div className="controls">
                        <div className="slide-number">
                            {currentSlideIdx + 1}/{presentationRegistry.numberOfSlides}
                        </div>

                        <Icon id="x" onClick={this.onClose} className="close"/>
                    </div>
                </div>

                <div ref={this.savePresentationAreaDimension} className="znai-presentation-slides-area">
                    {this.presentationAreaNode && (<SlidesLayout presentationRegistry={presentationRegistry}
                                                                 currentSlideIdx={currentSlideIdx}
                                                                 maxScaleRatio={maxScaleRatio}
                                                                 presentationArea={this.calcPresentationArea()}/>)
                    }
                </div>

                {showSlideNote && <div className="znai-presentation-footer">{slideVisibleNote}</div> }
            </div>
        )
    }

    calcPresentationArea() {
        return {
            width: this.presentationAreaNode.offsetWidth,
            height: this.presentationAreaNode.offsetHeight
        }
    }

    savePresentationAreaDimension = (node) => {
        this.presentationAreaNode = node
        this.forceUpdate()
    }

    componentDidMount() {
        document.addEventListener("keydown", this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener("keydown", this.keyDownHandler)
    }

    componentWillReceiveProps(props) {
        const {presentationRegistry} = this.props
        if (this.scrollToLastWithinPage && presentationRegistry !== props.presentationRegistry) {
            this.scrollToLastWithinPage = false
            this.setState({currentSlideIdx: props.presentationRegistry.numberOfSlides - 1})
        }
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
            this.setState({currentSlideIdx: newSlideIdx})
        }

    }

    incrementSlide() {
        const {presentationRegistry, onNextPage, hasNextPage} = this.props
        const {currentSlideIdx} = this.state
        const newSlideIdx = currentSlideIdx + 1

        if (newSlideIdx >= presentationRegistry.numberOfSlides) {
            if (hasNextPage) {
                this.setState({currentSlideIdx: 0})
                onNextPage()
            }
        } else {
            this.setState({currentSlideIdx: newSlideIdx})
        }
    }
}

export default Presentation
