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

import {fullResourcePath} from './resourcePath'

export {jsonPromise, jsonPromiseWithUrl}

function jsonPromise(docMeta, url) {
    url = fullResourcePath(docMeta.id, url)
    return jsonPromise(url)
}

function jsonPromiseWithUrl(url) {
    return new Promise((resolve, reject) => {
        fetch(url, {credentials: 'same-origin'}).then((response) => {
            response.json().then((json) => {
                resolve(json)
            }, (error) => {
                reject("can't parse data from: " + url + "; " + error)
            })
        }, (response) => reject("can't read data from: " + response))
    })
}
