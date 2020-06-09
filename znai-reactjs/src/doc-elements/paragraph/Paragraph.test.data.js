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

export function createAttentionParagraph(suffix) {
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
