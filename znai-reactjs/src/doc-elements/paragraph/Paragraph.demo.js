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
import {createAttentionParagraph} from "./Paragraph.test.data";

export function paragraphDemo(registry) {
    registry.add('with icon', () => (<elementsLibrary.DocElement content={createParagraphWithIcon()}
                                                                 elementsLibrary={elementsLibrary}/>))

    registry.add('note', () => (<elementsLibrary.DocElement content={createAttentionParagraph("Note:")}
                                                            elementsLibrary={elementsLibrary}/>))

    registry.add('warning', () => (<elementsLibrary.DocElement content={createAttentionParagraph("Warning:")}
                                                               elementsLibrary={elementsLibrary}/>))

    registry.add('avoid', () => (<elementsLibrary.DocElement content={createAttentionParagraph("Avoid:")}
                                                             elementsLibrary={elementsLibrary}/>))

    registry.add('question', () => (<elementsLibrary.DocElement content={createAttentionParagraph("Question:")}
                                                                elementsLibrary={elementsLibrary}/>))

    registry.add('exercise', () => (<elementsLibrary.DocElement content={createAttentionParagraph("Exercise:")}
                                                                elementsLibrary={elementsLibrary}/>))
}

function createParagraphWithIcon() {
    return [
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": "Long text followed by icon.",
                    "type": "SimpleText"
                },
                {
                    "id": "cloud",
                    "type": "Icon"
                },
                {
                    "text": "Text after the icon followed by another icon",
                    "type": "SimpleText"
                },
                {
                    "id": "git-pull-request",
                    "type": "Icon"
                },
            ]
        }
    ]
}
