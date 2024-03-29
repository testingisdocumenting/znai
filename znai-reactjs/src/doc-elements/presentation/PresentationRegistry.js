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

import React from 'react'
import {hasStickyPlacement, presentationStickPlacement} from "../meta/meta";

const emptySlide = createEmptySlide()

class PresentationRegistry {
    constructor(elementsLibrary, presentationElementHandlers, content) {
        this.elementsLibrary = elementsLibrary
        this.presentationElementHandlers = presentationElementHandlers
        this.numberOfSlides_ = 0
        this.slides = []
        this.stickySlides = []
        this.componentIdx = 0

        if (Array.isArray(content)) {
            this.registerSlides_(content)
        } else {
            this.registerSlide_(content)
        }
    }

    get numberOfSlides() {
        return this.numberOfSlides_
    }

    isEmpty() {
        return this.numberOfSlides_ === 0
    }

    handlerForDocElementType(type) {
        return this.presentationElementHandlers[type]
    }

    registerSlides_(items) {
        items.forEach(item => this.registerSlide_(item))
    }

    registerSlide_(item) {
        const type = item.type
        const props = item

        const presentationElementHandler = this.presentationElementHandlers[type]
        if (presentationElementHandler) {
            this.register(presentationElementHandler.component, props, presentationElementHandler)

            if (presentationElementHandler.ignoreNestedContent && presentationElementHandler.ignoreNestedContent(props)) {
                return;
            }
        }

        if (item.content) {
            this.registerSlides_(item.content)
        }
    }

    register(component, props, handler) {
        const numberOfSlides = handler.numberOfSlides(props);

        if (numberOfSlides === 0) {
            return
        }

        const slide = {
            componentIdx: this.componentIdx++,
            component: component,
            props: props,
            numberOfSlides: numberOfSlides
        }

        if (this.clearStickySlidesOnNextSlide) {
            this.stickySlides = []
            this.clearStickySlidesOnNextSlide = false
        }

        const stickPlacement = presentationStickPlacement(props.meta)

        const needToClearStickyState = (stickPlacement && stickPlacement.clear) ||
            props.type === 'Section'

        if (needToClearStickyState) {
            this.stickySlides = []
        }

        if (stickPlacement && !stickPlacement.temporary && hasStickyPlacement(stickPlacement)) {
            const lastSlideIdx = numberOfSlides - 1

            this.stickySlides.push({
                ...slide,
                stickPlacement,
                slideIdx: lastSlideIdx,
                info: extractInfo(lastSlideIdx)
            })
        } else if (!stickPlacement) {
            if (this.stickySlides.length > 0) {
                this.clearStickySlidesOnNextSlide = true
            }
        }

        for (let slideIdx = 0; slideIdx < numberOfSlides; slideIdx++) {
            this.slides.push({
                ...slide,
                slideIdx: slideIdx,
                info: extractInfo(slideIdx),
                stickySlides: stickySlidesWithoutCurrent(this.stickySlides)
            })
        }

        this.numberOfSlides_ += numberOfSlides

        function extractInfo(slideIdx) {
            return handler.slideInfoProvider ? handler.slideInfoProvider({...props, slideIdx: slideIdx}) : {}
        }

        function stickySlidesWithoutCurrent(stickySlides) {
            return stickySlides.filter(stickySlide => stickySlide.componentIdx !== slide.componentIdx)
        }
    }

    extractCombinedSlideInfo(pageLocalSlideIdx) {
        if (pageLocalSlideIdx < 0 || pageLocalSlideIdx >= this.slides.length) {
            return {pageTitle: '', sectionTitle: '', slideVisibleNote: ''}
        }

        const slideInfo = this.slides[pageLocalSlideIdx].info

        const slideVisibleNote = slideInfo.slideVisibleNote
        let pageTitle = ""
        let sectionTitle = ""

        for (let i = pageLocalSlideIdx; i >= 0; i--) {
            const slide = this.slides[i];

            if (slide.info.pageTitle && !pageTitle) {
                pageTitle = slide.info.pageTitle
            }

            if (slide.info.sectionTitle && !sectionTitle) {
                sectionTitle = slide.info.sectionTitle
            }
        }

        return {pageTitle, sectionTitle, slideVisibleNote}
    }

    renderSlide(slide, renderOpts) {
        return <slide.component {...slide.props}
                                {...renderOpts}
                                elementsLibrary={this.elementsLibrary}
                                slideIdx={slide.slideIdx}
                                isPresentation={true}/>
    }

    slideByIdx(pageLocalSlideIdx) {
        return this.slides[pageLocalSlideIdx] || emptySlide
    }

    slideComponentStartIdxByIdx(pageLocalSlideIdx) {
        let idx = pageLocalSlideIdx
        for (; idx > 0; idx--) {
            if (this.slideByIdx(idx - 1).componentIdx !== this.slideByIdx(idx).componentIdx) {
                return idx
            }
        }

        return 0
    }

    findSlideIdxBySectionId(sectionId) {
        if (!sectionId) {
            return 0
        }

        for (let slideIdx = 0; slideIdx < this.slides.length; slideIdx++) {
            const slide = this.slides[slideIdx]
            if (slide.info.sectionId === sectionId) {
                return slideIdx;
            }
        }

        return 0;
    }
}

function createEmptySlide() {
    return {
        componentIdx: -1,
        component: () => <div/>,
        props: {},
        numberOfSlides: 1,
        slideIdx: 0,
        stickySlides: [],
        info: {}
    }
}

export default PresentationRegistry
