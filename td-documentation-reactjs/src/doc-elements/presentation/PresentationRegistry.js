import React from 'react'

class PresentationRegistry {
    constructor(elementsLibrary, presentationElementHandlers, page) {
        this.elementsLibrary = elementsLibrary
        this.presentationElementHandlers = presentationElementHandlers
        this.numberOfSlides_ = 0
        this.slideComponents = []

        this.registerComponent_(page)
    }

    get numberOfSlides() {
        return this.numberOfSlides_
    }

    isEmpty() {
        return this.numberOfSlides_ === 0
    }

    registerComponents_(items) {
        items.forEach(item => this.registerComponent_(item))
    }

    registerComponent_(item) {
        const type = item.type
        const props = item

        const presentationElementHandler = this.presentationElementHandlers[type]
        if (presentationElementHandler) {
            this.register(presentationElementHandler.component, props, presentationElementHandler.numberOfSlides(props))
        }

        if (item.content) {
            this.registerComponents_(item.content)
        }
    }

    register(component, props, numberOfSlides) {
        const slideComponent = {
            component: component,
            props: props,
            numberOfSlides: numberOfSlides
        }

        this.slideComponents.push(slideComponent)
        this.numberOfSlides_ += numberOfSlides
    }

    componentToRender(slideIdx) {
        const {slideComponent, componentIdx} = this.findComponentAndIdx(slideIdx)
        const numberOfSlidesToSkip = this.calcNumberOfSlidesToSkip(componentIdx)
        return <slideComponent.component {...slideComponent.props}
                                         elementsLibrary={this.elementsLibrary}
                                         slideIdx={slideIdx - numberOfSlidesToSkip}/>
    }

    calcNumberOfSlidesToSkip(componentIdx) {
        let numberOfSlides = 0
        for (let i = 0; i < componentIdx; i++) {
            numberOfSlides += this.slideComponents[i].numberOfSlides
        }

        return numberOfSlides
    }

    findComponentAndIdx(slideNumber) {
        let startSlideIdx = 0
        for (let componentIdx = 0, len = this.slideComponents.length; componentIdx < len; componentIdx++) {
            const slideComponent = this.slideComponents[componentIdx]
            const endSlideIdx = startSlideIdx + slideComponent.numberOfSlides

            if (slideNumber >= startSlideIdx && slideNumber < endSlideIdx) {
                return {slideComponent, componentIdx}
            }

            startSlideIdx = endSlideIdx
        }

        return {slideComponent: this.slideComponents[this.slideComponents.length - 1],
            componentIdx: this.slideComponents.length - 1}
    }
}

export default PresentationRegistry
