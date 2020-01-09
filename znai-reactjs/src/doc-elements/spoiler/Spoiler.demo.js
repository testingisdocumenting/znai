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

import * as React from 'react'
import {Spoiler} from './Spoiler'
import {elementsLibrary} from '../DefaultElementsLibrary'

export function spoilerDemo(registry) {
    registry
        .add('regular text', () => <Spoiler title="Press to reveal"
                                            content={paragraphContent()}
                                            elementsLibrary={elementsLibrary}/>)
        .add('code snippet', () => <Spoiler title="Press to show code"
                                            content={codeContent()}
                                            elementsLibrary={elementsLibrary}/>)
        .add('code snippet with bullets', () => <Spoiler title="reveal"
                                                         content={codeWithBulletsContent()}
                                                         elementsLibrary={elementsLibrary}/>)
        .add('tabs', () => <Spoiler title="Reveal tabs"
                                    content={tabsContent()}
                                    elementsLibrary={elementsLibrary}/>)
}

function paragraphContent() {
    return [
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": "line of text line of text line of text line of text line of text line of text line of text " +
                        "line of text line of text line of text line of text",
                    "type": "SimpleText"
                }
            ]
        },
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": "another of text another of text another of text another of text another of text another of " +
                        "text another of text another of text another of text another of text another of text",
                    "type": "SimpleText"
                }
            ]
        }]
}

function codeContent() {
    return [
        {
            "type": "Snippet",
            "snippet": 'class Hello {\n ...\n}'
        }
    ]
}

function codeWithBulletsContent() {
    return [
        {
            "type": "Snippet",
            "snippet": "class InternationalPriceService implements PriceService {\n" +
                "    private static void main(String... args) {\n" +
                "        ... // code goes here\n" +
                "    } // code stops here\n" +
                "}\n",
            "commentsType": "inline"
        }
    ]
}

function tabsContent() {
    return [
        {
            type: "Tabs",
            tabsContent: [
                {
                    "name": "cpp",
                    "content": [
                        {
                            "lang": "cpp",
                            "snippet": "code snippet \n",
                            "lineNumber": "",
                            "type": "Snippet"
                        },
                    ]
                }
            ]
        }
    ]
}