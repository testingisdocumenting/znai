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

import {selectedTextExtensions} from './selected-text-extensions/SelectedTextExtensions'
import {jsonPromise} from "../utils/json"

let docMeta = {}

function setDocMeta(newDocMeta) {
    docMeta = {...newDocMeta}
    registerExtensions()
}

function registerExtensions() {
    if (docMeta.hasOwnProperty('hipchatRoom')) {
        selectedTextExtensions.register({
            name: 'Ask in HipChat',
            action: (args) => console.log('hipchat action:', args)
        })
    }
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
        supportLinkPromise = new Promise((resolve, reject) => resolve(support.link));
    } else if (support && support.urlToFetchSupportLink) {
        supportLinkPromise = jsonPromise(support.urlToFetchSupportLink).then(supportMeta => supportMeta.link)
    } else {
        supportLinkPromise = new Promise((resolve, reject) => resolve(null));
    }
    return supportLinkPromise
}

export {setDocMeta, isPreviewEnabled, getDocId, getSupportLinkPromise}
