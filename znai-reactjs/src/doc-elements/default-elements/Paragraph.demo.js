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

import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'

export function paragraphDemo(registry) {
    registry.add('note', () => (<elementsLibrary.DocElement content={createParagraph("Note:")}
                                                            elementsLibrary={elementsLibrary}/>))

    registry.add('warning', () => (<elementsLibrary.DocElement content={createParagraph("Warning:")}
                                                               elementsLibrary={elementsLibrary}/>))

    registry.add('avoid', () => (<elementsLibrary.DocElement content={createParagraph("Avoid:")}
                                                             elementsLibrary={elementsLibrary}/>))

    registry.add('question', () => (<elementsLibrary.DocElement content={createParagraph("Question:")}
                                                                elementsLibrary={elementsLibrary}/>))

    registry.add('inside bullet list', () => (<elementsLibrary.DocElement content={createListWithParagraphs()}
                                                                          elementsLibrary={elementsLibrary}/>))
}

function createParagraph(suffix) {
    return [
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": `${suffix} It is very easy to add a code snippet or an output result.`,
                    "type": "SimpleText"
                },
                {
                    "type": "SoftLineBreak"
                },
                {
                    "text": "All you have to do is indent your code with 4 spaces inside your markdown document and",
                    "type": "SimpleText"
                },
                {
                    "type": "SoftLineBreak"
                },
                {
                    "text": "your code will be rendered like this.",
                    "type": "SimpleText"
                }
            ]
        }
    ]
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
