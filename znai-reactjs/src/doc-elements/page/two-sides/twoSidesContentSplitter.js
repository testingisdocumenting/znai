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