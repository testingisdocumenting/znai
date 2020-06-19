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

class PresentationRegistry {
    constructor(elementsLibrary, presentationElementHandlers, content) {
        this.elementsLibrary = elementsLibrary
        this.presentationElementHandlers = presentationElementHandlers
        this.numberOfSlides_ = 0
        this.slides = []

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
        }

        if (item.content) {
            this.registerSlides_(item.content)
        }
    }

    register(component, props, handler) {
        let numberOfSlides = handler.numberOfSlides(props);

        const slide = {
            component: component,
            props: props,
            numberOfSlides: numberOfSlides
        }

        for (let slideIdx = 0; slideIdx < numberOfSlides; slideIdx++) {
            this.slides.push({...slide, slideIdx: slideIdx,
                info: handler.slideInfoProvider ? handler.slideInfoProvider({...props, slideIdx: slideIdx}) : {} })
        }

        this.numberOfSlides_ += numberOfSlides
    }

    extractCombinedSlideInfo(pageLocalSlideIdx) {
        const slideInfo = pageLocalSlideIdx >= 0 ? this.slides[pageLocalSlideIdx].info : {}

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

    /**
     * renders component with a slide content
     * @param pageLocalSlideIdx slide idx local for a page. At the start of each page it equals 0
     * @param scaleRatio slide scale ratio
     * @returns {React.ReactNode}
     */
    renderComponent({pageLocalSlideIdx, scaleRatio}) {
        const slide = this.slides[pageLocalSlideIdx]

        return <slide.component {...slide.props}
                                elementsLibrary={this.elementsLibrary}
                                slideIdx={slide.slideIdx}
                                slideScaleRatio={scaleRatio}
                                isPresentation={true}/>
    }

    slideByIdx(pageLocalSlideIdx) {
        return this.slides[pageLocalSlideIdx]
    }
}

export default PresentationRegistry
