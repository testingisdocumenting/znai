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

export function snippetWithScrollPresentationDemo(registry) {
    registry
        .add('just snippet', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            title: 'Long source code',
            numberOfVisibleLines: 11,
            snippet: javaCode()
        }]))
        .add('with sticky slide', createPresentationDemo([
            {
                type: 'Snippet',
                lang: 'java',
                title: 'Long source code',
                numberOfVisibleLines: 11,
                snippet: javaCode(),
                meta: {
                    stickySlide: "left"
                }
            },
            {
                type: 'Snippet',
                lang: 'java',
                title: 'Long source code',
                numberOfVisibleLines: 11,
                snippet: javaCode()
            }
        ]))
}

function javaCode() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        System.out.println("hello 1");\n' +
        '        System.out.println("hello 2");\n' +
        '        System.out.println("hello 3");\n' +
        '        System.out.println("hello 4");\n' +
        '        System.out.println("hello 5");\n' +
        '        System.out.println("hello 6");\n' +
        '        System.out.println("hello 7");\n' +
        '        System.out.println("hello 8");\n' +
        '        System.out.println("hello 9");\n' +
        '        System.out.println("hello 10");\n' +
        '        System.out.println("hello 11");\n' +
        '        System.out.println("hello 12");\n' +
        '        System.out.println("hello 13");\n' +
        '        System.out.println("hello 14");\n' +
        '        System.out.println("hello 15");\n' +
        '        System.out.println("hello 16");\n' +
        '        System.out.println("hello 17");\n' +
        '        System.out.println("hello 18");\n' +
        '        System.out.println("hello 19");\n' +
        '        System.out.println("hello 20");\n' +
        '        System.out.println("hello 21");\n' +
        '        System.out.println("hello 22");\n' +
        '        System.out.println("hello 23");\n' +
        '        System.out.println("hello 24");\n' +
        '        System.out.println("hello 25");\n' +
        '        System.out.println("hello 26");\n' +
        '        System.out.println("hello 27");\n' +
        '        System.out.println("hello world to make line longer that others line to test jump 27");\n' +
        '        System.out.println("hello 28");\n' +
        '        System.out.println("hello 29");\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
}
