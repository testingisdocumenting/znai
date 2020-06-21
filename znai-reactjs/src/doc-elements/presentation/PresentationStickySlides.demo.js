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

export function presentationStickySlidesDemo(registry) {
    registry
        .add('left to right', createPresentationDemo([
            {
                type: 'Snippet',
                lang: 'java',
                snippet: javaCode(),
                highlight: [2],
                meta: {
                    stickySlide: 'left 30%'
                }
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: pythonCode(),
                meta: {
                    stickySlide: 'left 30%'
                }
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: anotherPythonCode(),
            }
        ], {
            slideIdx: 2,
        }))
        .add('top to bottom', createPresentationDemo([
            {
                type: 'Snippet',
                lang: 'java',
                snippet: javaCode(),
                highlight: [2],
                meta: {
                    stickySlide: 'top 30%'
                }
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: pythonCode(),
                meta: {
                    stickySlide: 'top 30%'
                }
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: anotherPythonCode(),
                highlight: [1],
            }
        ], {
            slideIdx: 2,
        }))
        .add('left to right, top to bottom', createPresentationDemo([
            {
                type: 'Snippet',
                lang: 'java',
                snippet: javaCode(),
                highlight: [2],
                meta: {
                    stickySlide: 'left'
                }
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: pythonCode(),
                meta: {
                    stickySlide: 'top'
                }
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: anotherPythonCode(),
                highlight: [1],
            },
            {
                type: 'Snippet',
                lang: 'python',
                snippet: anotherPythonCode(),
                highlight: [0],
            }
        ], {
            slideIdx: 2,
        }))
        .add('top to bottom paragraphs', createPresentationDemo([
            {
                type: 'Paragraph',
                meta: {
                    stickySlide: 'top 30%'
                },
                content: [
                    {
                        type: 'SimpleText',
                        text: 'Question: what is sticky slide'
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "All you have to do is indent your code with 4 spaces inside your markdown document and",
                        "type": "SimpleText"
                    },
                ]
            },
            {
                type: 'Paragraph',
                meta: {
                    stickySlide: 'top 30%'
                },
                content: [{
                    type: 'SimpleText',
                    text: 'Question: what is multiple slide'
                }]
            },
            {
                type: 'Paragraph',
                meta: {
                    stickySlide: 'top'
                },
                content: [{
                    type: 'SimpleText',
                    text: 'Question: what is '
                }]
            },
        ], {
            slideIdx: 0,
        }))
        .add('left to right quotes', createPresentationDemo([
            {
                type: 'BlockQuote',
                meta: {
                    stickySlide: 'left 30%'
                },
                content: [
                    {
                        type: 'Paragraph',
                        content: [{
                            type: 'SimpleText',
                            text: 'quote text quote text'
                        }]
                    }
                ]
            },
            {
                type: 'BlockQuote',
                meta: {
                    stickySlide: 'left 30%'
                },
                content: [
                    {
                        type: 'Paragraph',
                        content: [{
                            type: 'SimpleText',
                            text: 'another quote'
                        }]
                    }
                ]
            },
            {
                type: 'BlockQuote',
                meta: {
                    stickySlide: 'left'
                },
                content: [
                    {
                        type: 'Paragraph',
                        content: [{
                            type: 'SimpleText',
                            text: 'third quote'
                        }]
                    }
                ]
            },
        ], {
            slideIdx: 0,
        }))
}

function javaCode() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        System.out.println("hello");\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
}

function pythonCode() {
    return 'def method:\n' +
        '    print("hello")\n'
}

function anotherPythonCode() {
    return 'def method:\n' +
        '    print("world")\n'
}
