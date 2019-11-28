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

import * as Promise from 'promise'
import {getDocId} from '../docMeta';

const index = {dirName: '', fileName: 'index'};

class DocumentationNavigation {
    constructor() {
        this.listeners = []

        // server side rendering guard
        if (window.addEventListener) {
            window.addEventListener('popstate', (e) => {
                this.notifyNewUrl(document.location.pathname + (document.location.hash))
            })
        }
    }

    addUrlChangeListener(listener) {
        this.listeners.push(listener)
    }

    removeUrlChangeListener(listener) {
        const idx = this.listeners.indexOf(listener)
        if (idx === -1) {
            return
        }

        this.listeners.splice(idx, 1)
    }

    fullPageUrl(relativePageUrl) {
        if (relativePageUrl.indexOf('#') === 0) {
            return relativePageUrl
        }

        return "/" + (getDocId()  ? getDocId() : "") +
            (relativePageUrl.indexOf('/') === 0 ? '' : '/') +
            relativePageUrl
    }

    buildUrl(id) {
        return  this.fullPageUrl(id.dirName + "/" + id.fileName +
            (id.pageSectionId ? ("#" + id.pageSectionId) : ""))
    }

    navigateToPage(id) {
        return this.navigateToUrl(this.buildUrl(id))
    }

    navigateToUrl(url) {
        window.history.pushState({}, null, url)
        return this.notifyNewUrl(url)
    }

    scrollToAnchor(anchorId) {
        const anchor = document.getElementById(anchorId)
        if (anchor) {
            anchor.scrollIntoView()
        }
    }

    notifyNewUrl(url) {
        const promises = this.listeners.map((l) => l(url))
        return Promise.all(promises)
    }

    currentPageLocation() {
        return window.location ?
            {...this.extractPageLocation(window.location.pathname),
                anchorId: window.location.hash.length ? window.location.hash.substr(1) : ""}:
            "/server/side"
    }

    extractPageLocation(url) {
        const hashIdx = url.indexOf("#");
        const anchorId = hashIdx >= 0 ? url.substr(hashIdx + 1) : ""
        url = hashIdx >= 0 ? url.substr(0, hashIdx) : url

        const parts = url.split("/").filter(p => p !== ".." && p.length > 0)

        // url starts with /<doc-id> ?
        if (url.substr(1) === getDocId()) {
            return index
        }

        if (parts.length < 2) {
            return index
        }

        // something/dir-name/file-name#id
        // /dir-name/file-name
        return {dirName: parts[parts.length - 2], fileName: parts[parts.length - 1], anchorId: anchorId}
    }
}

const documentationNavigation = new DocumentationNavigation()

export {documentationNavigation}
