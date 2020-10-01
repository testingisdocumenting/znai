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
import {createAttentionParagraph, createShortAttentionParagraph} from "./Paragraph.test.data";

export function paragraphPresentationDemo(registry) {
    registry
        .add('question', createPresentationDemo(createAttentionParagraph('Question:')))
        .add('short exercise', createPresentationDemo(createShortAttentionParagraph('Exercise:')))
        .add('forced paragraph', createPresentationDemo(createForcedParagraph()))
}

export function createForcedParagraph() {
    return [
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": "simple paragraph with text simple paragraph with text simple paragraph with text simple " +
                        "paragraph with text simple paragraph with text simple paragraph with text ",
                    "type": "SimpleText",
                    "meta": {
                        "presentationParagraph": "default"
                    }
                },
            ],
        }
    ]
}
