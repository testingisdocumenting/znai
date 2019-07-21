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

import {getDocId} from '../docMeta'

class TextSelection {
    constructor() {
        this.listeners = []
    }

    addListener(listener) {
        this.listeners.push(listener)
    }

    startSelection({pageSectionTitle, pageSectionId}) {
        this.selectionSectionInfo = {pageSectionTitle, pageSectionId}
    }

    endSelection({tocItem, startNode, text}) {
        const url = "/" + getDocId() + "/" + tocItem.dirName + "/" + tocItem.fileName +
            (this.selectedPageSectionId ? "#" + this.selectedPageSectionId : "")

        this.listeners.forEach(l => l({...this.selectionSectionInfo,
            sectionTitle: tocItem.sectionTitle,
            pageTitle: tocItem.pageTitle,
            startNode, text,
            url}))

        this.selectionSectionInfo = {}
    }

    get selectedPageSectionId() {
        return this.selectionSectionInfo.pageSectionId
    }

    clear() {
        this.selectionSectionInfo = {}
        this.listeners.forEach(l => l({
            sectionTitle: null,
            pageTitle: null,
            pageSectionTitle: null,
            startNode: null,
            text: null}))
    }
}

const textSelection = new TextSelection()

export {textSelection}
