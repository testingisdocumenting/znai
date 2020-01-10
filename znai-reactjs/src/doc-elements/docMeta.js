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

import {jsonPromise} from "../utils/json"

let docMeta = {}

function setDocMeta(newDocMeta) {
    docMeta = {...newDocMeta}
}

function getDocMeta() {
    return {...docMeta}
}

function addDocMeta(newDocMeta) {
    docMeta = Object.assign({}, docMeta, {...newDocMeta})
    return getDocMeta()
}

function isPreviewEnabled() {
    return docMeta.previewEnabled
}

function getDocId() {
    return docMeta.id
}

let supportLinkPromise = null;

function getSupportLinkPromise() {
    if (supportLinkPromise) {
        return supportLinkPromise
    }
    const support = docMeta.support
    if (support && support.link) {
        supportLinkPromise = new Promise((resolve, reject) => resolve(support.link))
    } else if (support && support.urlToFetchSupportLink) {
        supportLinkPromise = jsonPromise(support.urlToFetchSupportLink(getDocMeta())).then(supportMeta => supportMeta.link)
    } else {
        supportLinkPromise = new Promise((resolve, reject) => resolve(null))
    }
    return supportLinkPromise
}

export {setDocMeta, addDocMeta, getDocMeta, isPreviewEnabled, getDocId, getSupportLinkPromise}
