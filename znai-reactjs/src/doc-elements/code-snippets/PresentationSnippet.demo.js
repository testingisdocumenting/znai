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

export function snippetPresentationDemo(registry) {
    registry
        .add('just snippet', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            snippet: javaCode(),
        }]))
        .add('highlights', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            snippet: javaCode(),
            highlight: [1, 3]
        }]))
        .add('highlights all at once', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            snippet: javaCode(),
            highlight: [1, 3],
            meta: {allAtOnce: true}
        }]))
        .add('reveal line stop', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            snippet: javaCode(),
            revealLineStop: [0, 2, 3]
        }]))
        .add('reveal line stop and highlight', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            snippet: javaCode(),
            revealLineStop: [0, 2, 3],
            highlight: [0, 5]
        }]))
        .add('reveal line stop and highlight all at once', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            snippet: javaCode(),
            revealLineStop: [0, 2, 3],
            highlight: [0, 5],
            meta: {allAtOnce: true}
        }]))
}

function javaCode() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        System.out.println("hello");\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
}
