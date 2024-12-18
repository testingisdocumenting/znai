/*
 * Copyright 2024 znai maintainers
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

import {getAllPagesPromise} from "../allPages"

import * as Promise from 'promise'
import Search from './Search'

export {getSearchPromise}

let searchPromise = null

function getSearchPromise(docMeta) {
    if (searchPromise) {
        return searchPromise
    }

    searchPromise = getAllPagesPromise(docMeta).then((allPages) => {
        return new Search(allPages)
    }, (error) => {
        console.error(error)
        return new Promise.reject("both search index and pages needs to be loaded")
    })

    return searchPromise
}
