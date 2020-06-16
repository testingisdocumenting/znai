/*
 * Copyright 2020 znai maintainers
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

export function subHeadingPresentationDemo(registry) {
    registry
        .add('long', createPresentationDemo([longContent()]))
        .add('short', createPresentationDemo([shortContent()]))
        .add('long single', createPresentationDemo([longSingleWord()]))
        .add('with empty title section', createPresentationDemo([emptySection()]))
}

function longContent() {
    return {
        'title': 'Long Long Long SubHeading',
        'id': 'sub',
        'level': 2,
        'type': 'SubHeading'
    }
}

function shortContent() {
    return {
        'title': 'Short',
        'id': 'sub',
        'level': 2,
        'type': 'SubHeading'
    }
}

function longSingleWord() {
    return {
        'title': 'LongSingleWordLongSingleWordLong',
        'id': 'sub',
        'level': 2,
        'type': 'SubHeading'
    }
}

function emptySection() {
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
            "title": "",
            "id": "title",
            "type": "Section",
            "content": [
                {
                    'title': 'Long Long Long SubHeading',
                    'id': 'sub',
                    'level': 2,
                    'type': 'SubHeading'
                }
            ]
        }]
    }
}
