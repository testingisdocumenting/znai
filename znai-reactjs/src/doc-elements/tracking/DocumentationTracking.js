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

class DocumentationTracking {
    constructor() {
        this.currentPageId = {}
        this.listeners = []
    }

    addListener(listener) {
        this.listeners.push(listener)
    }

    onPageOpen(pageId) {
        this.currentPageId = pageId
        this.notifyListeners('onPageOpen', pageId)
    }

    onNextPage() {
        this.notifyListeners('onNextPage', this.currentPageId)
    }

    onPrevPage() {
        this.notifyListeners('onPrevPage', this.currentPageId)
    }

    onScrollToSection(sectionIdTitle) {
        this.notifyListeners('onScrollToSection', this.currentPageId, sectionIdTitle)
    }

    onTocItemSelect(tocItem) {
        this.notifyListeners('onTocItemSelect', this.currentPageId, tocItem)
    }

    onLinkClick(url) {
        this.notifyListeners('onLinkClick', this.currentPageId, url)
    }

    onSearchResultSelect(query, selectedPageId) {
        this.notifyListeners('onSearchResultSelect', this.currentPageId, query, selectedPageId)
    }

    onPresentationOpen() {
        this.notifyListeners('onPresentationOpen', this.currentPageId)
    }

    notifyListeners(methodName, ...args) {
        this.listeners.forEach(listener => safeCall(listener, methodName, args))
    }
}

function safeCall(listener, methodName, args) {
    if (!listener[methodName]) {
        return
    }

    listener[methodName].apply(listener, args)
}

const documentationTracking = new DocumentationTracking()

export {DocumentationTracking, documentationTracking}