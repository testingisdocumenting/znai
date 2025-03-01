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

import {createPresentationDemo} from '../demo-utils/PresentationDemo'

const longNameDocMeta = {
    id: "znai",
    title: "Long Documentation Title Multi",
    type: "User Guide"
}

const shortNameDocMeta = {
    id: "znai",
    title: "Sho",
    type: "User Guide"
}

export function presentationDemo(registry) {
    registry
        .add('slide note', createPresentationDemo([longHeadersAndSideNote()], {
            slideIdx: 4,
            docMeta: longNameDocMeta
        }))
        .add('long page title', createPresentationDemo([longHeadersAndSideNote()], {
            slideIdx: 0,
            docMeta: longNameDocMeta
        }))
        .add('long section title', createPresentationDemo([longHeadersAndSideNote()], {
            slideIdx: 1,
            docMeta: longNameDocMeta
        }))
        .add('short page title', createPresentationDemo([shortHeaders()], {
            slideIdx: 0,
            docMeta: shortNameDocMeta
        }))
        .add('long word title', createPresentationDemo([singleLongWordHeaders()], {
            slideIdx: 0,
            docMeta: shortNameDocMeta
        }))
}

function longHeadersAndSideNote() {
    return {
        "type": "Page",
        "tocItem": {
            "sectionTitle": "Section Title",
            "pageTitle": "Long Page Title Long Page Title",
            "pageMeta": {},
            "fileName": "file-name",
            "dirName": "dir-name",
            "pageSectionIdTitles": [],
        },
        "content": [{
            "title": "Long Long Title Very Long Title Yes",
            "id": "title",
            "type": "Section",
            "content": [{
                'lang': 'javascript',
                'snippet': 'class JsClass {\n    constructor() { // new syntax for constructor\n    }\n}\n\nexport default JsClass // new syntax for ES6 modules',
                'commentsType': 'inline',
                'type': 'Snippet'
            },
                {
                    'title': 'Long Long Long SubHeading',
                    'id': 'sub',
                    'level': 2,
                    'type': 'SubHeading'
                }]
        }]
    }
}

function shortHeaders() {
    return {
        "type": "Page",
        "tocItem": {
            "sectionTitle": "Section Title",
            "pageTitle": "Shor",
            "pageMeta": {},
            "fileName": "file-name",
            "dirName": "dir-name",
            "pageSectionIdTitles": [],
        },
        "content": [{
            "title": "Shor",
            "id": "title",
            "type": "Section",
            "content": []
        }]
    }
}

function singleLongWordHeaders() {
    return {
        "type": "Page",
        "tocItem": {
            "sectionTitle": "SectionTitleLong",
            "pageTitle": "SingleLongWordTitle",
            "pageMeta": {},
            "fileName": "file-name",
            "dirName": "dir-name",
            "pageSectionIdTitles": [],
        },
        "content": [{
            "title": "SingleLongWordTitle",
            "id": "title",
            "type": "Section",
            "content": []
        }]
    }
}