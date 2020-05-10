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

import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'

export function bulletListsDemo(registry) {
    registry.add('with multi paragraph', () => (<elementsLibrary.DocElement content={createListWithParagraphs()}
                                                                            elementsLibrary={elementsLibrary}/>))

    registry.add('with icons', () => <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                                                 content={bulletsWithIcons()}/>)
}

function createListWithParagraphs() {
    return [ {
        "bulletMarker" : "*",
        "tight" : false,
        "type" : "BulletList",
        "content" : [ {
            "type" : "ListItem",
            "content" : [ {
                "type" : "Paragraph",
                "content" : [ {
                    "text" : "First list item's first paragraph.",
                    "type" : "SimpleText"
                }, {
                    "type" : "SoftLineBreak"
                }, {
                    "text" : "This is still part of the first paragraph.",
                    "type" : "SimpleText"
                } ]
            }, {
                "type" : "Paragraph",
                "content" : [ {
                    "text" : "First list item's second paragraph",
                    "type" : "SimpleText"
                } ]
            } ]
        }, {
            "type" : "ListItem",
            "content" : [ {
                "type" : "Paragraph",
                "content" : [ {
                    "text" : "Second list item's first paragraph",
                    "type" : "SimpleText"
                } ]
            } ]
        } ]
    } ]
}

function bulletsWithIcons() {
    return [{
        "bulletMarker": "*",
        "tight": true,
        "type": "BulletList",
        "content": [
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "type": "Icon",
                                "id": "search"
                            },
                            {
                                "text": "Something",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "type": "Icon",
                                "id": "time"
                            },
                            {
                                "text": "Additional Additional Additional Additional Additional Additional Additional Additional Additional Additional Additional Additional",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "type": "Icon",
                                "id": "thumbs-down"
                            },
                            {
                                "type": "Emphasis",
                                "content": [
                                    {
                                        "text": "Inject",
                                        "type": "SimpleText"
                                    }
                                ]
                            },
                            {
                                "text": " ",
                                "type": "SimpleText"
                            },
                            {
                                "code": "DAO",
                                "type": "InlinedCode"
                            },
                            {
                                "text": " to decouple",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ]
    }]
}
