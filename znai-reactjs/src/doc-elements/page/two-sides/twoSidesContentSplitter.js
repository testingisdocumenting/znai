import {elementMetaValue} from '../../meta/meta'

export function splitContentInTwoPartsSubsections(content) {
    const subSections = []

    let currentLeft
    let currentRight
    let isCurrentRight
    let currentSubSection

    startSubSection()

    content.forEach(handleDocElement)
    return subSections

    function handleDocElement(docElement) {
        const isElementOnRight = true === elementMetaValue(docElement, "rightSide")

        if (!isCurrentRight && !isElementOnRight) {
            currentLeft.push(docElement)
            return
        }

        if (!isCurrentRight && isElementOnRight) {
            isCurrentRight = true
            currentRight.push(docElement)
            return
        }

        if (isCurrentRight && isElementOnRight) {
            currentRight.push(docElement)
            return
        }

        if (isCurrentRight && !isElementOnRight) {
            startSubSection()
            currentLeft.push(docElement)
        }
    }

    function startSubSection() {
        currentLeft = []
        currentRight = []
        isCurrentRight = false
        currentSubSection = {left: currentLeft, right: currentRight}

        subSections.push(currentSubSection)
    }
}