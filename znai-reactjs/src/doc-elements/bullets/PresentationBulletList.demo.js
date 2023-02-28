/*
 * Copyright 2020 znai maintainers
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

export function bulletListPresentationDemo(registry) {
    const content = createBulletContent()

    registry
      .add('reveal boxes', createPresentationDemo([{
            type: 'BulletList',
            meta: {
                presentationBulletListType: 'RevealBoxes'
            },
            content
        }])
      )
      .add('grid boxes', createPresentationDemo([{
            type: 'BulletList',
            meta: {
                presentationBulletListType: 'Grid'
            },
            content
        }])
      )
      .add('horizontal stripes', createPresentationDemo([{
            type: 'BulletList',
            meta: {
                presentationBulletListType: 'HorizontalStripes'
            },
            content
        }])
      )
      .add('horizontal stripes with icons', createPresentationDemo([{
            type: 'BulletList',
            meta: {
                presentationBulletListType: 'HorizontalStripes'
            },
            content: bulletListWithIcons()
        }])
      )
}

function createBulletContent() {
    return [
        {
            "type": "ListItem",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [{
                        "text": "Rest API",
                        "type": "SimpleText"
                    }]
                }
            ]
        },
        {
            "type": "ListItem",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [{
                        "text": "Web UI",
                        "type": "SimpleText"
                    }]
                }
            ]
        },
        {
            "type": "ListItem",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [{
                        "text": "Command Line",
                        "type": "SimpleText"
                    }]
                }
            ]
        }
    ]
}

function bulletListWithIcons() {
    return [
        {
            "type": "ListItem",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "id": "time",
                    "type": "Icon"
                }, {
                    "text": " Lost time for users",
                    "type": "SimpleText"
                }]
            }]
        },
        {
            "type": "ListItem",
            "content": [{
                "type": "Paragraph",
                "content": [{
                    "id": "thumbs-down",
                    "type": "Icon"
                }, {
                    "text": " Reputational damage",
                    "type": "SimpleText"
                }]
            }]
        }
    ]
}