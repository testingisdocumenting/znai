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

import {jsonPromise} from '../utils/json'
import pageContentProcessor from './pageContentProcessor.js'
import {fullResourcePath} from "../utils/resourcePath";
import {areTocItemEquals} from './structure/tocItem'

// we don't load all-pages using regular javascript script tag
// this is because we want to ui provide to be responsive while data is loading in the background
//

let pagesPromise = null
function getAllPagesPromise(docMeta) {
    if (pagesPromise) {
        return pagesPromise
    }

    const url = fullResourcePath(docMeta.id, "all-pages.json")
    pagesPromise = jsonPromise(url).then(allPages => {
        return new AllPages(allPages.map(page => {
            return {...page, content: pageContentProcessor.process(page.content)}
        }))
    })

    return pagesPromise
}

class AllPages {
    constructor(pages) {
        this.pages = pages
    }

    update(newPage) {
        const foundPage = this.find(newPage.tocItem)

        if (foundPage) {
            foundPage.content = newPage.content
        } else {
            this.pages.push(newPage)
        }
    }

    find(tocItem) {
        return this.pages.find((page) => areTocItemEquals(page.tocItem, tocItem))
    }

    remove(tocItem) {
        const idx = this.pages.findIndex(page => areTocItemEquals(page.tocItem, tocItem))
        if (idx !== -1) {
            this.pages.splice(idx, 1)
        }
    }
}

export {getAllPagesPromise, AllPages}
