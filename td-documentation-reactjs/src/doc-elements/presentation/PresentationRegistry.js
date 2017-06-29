import React from 'react'
import RenderingMeta from '../meta/RenderingMeta'

class PresentationRegistry {
    constructor(elementsLibrary, presentationElementHandlers, content) {
        this.elementsLibrary = elementsLibrary
        this.renderingMeta = new RenderingMeta()
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

        if (type === 'Meta') {
            this.renderingMeta = this.renderingMeta.register(item)
            return;
        }

        const presentationElementHandler = this.presentationElementHandlers[type]
        if (presentationElementHandler) {
            const propsWithRenderingMeta = {renderingMeta: this.renderingMeta, ...props}
            this.register(presentationElementHandler.component, propsWithRenderingMeta, presentationElementHandler)
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

    extractSlideInfo(pageLocalSlideIdx) {
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
     * @returns {XML}
     */
    renderedComponent(pageLocalSlideIdx) {
        const slide = this.slides[pageLocalSlideIdx]

        return <slide.component {...slide.props}
                                elementsLibrary={this.elementsLibrary}
                                slideIdx={slide.slideIdx}
                                isPresentation={true}/>
    }

    slideByIdx(pageLocalSlideIdx) {
        return this.slides[pageLocalSlideIdx]
    }
}

export default PresentationRegistry
