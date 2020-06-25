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
        .add('wide snippet', createPresentationDemo([{
            lang : 'java',
            snippet : 'class InternationalPriceService implements PriceService {\n' +
                '    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {\n' +
                '        ...\n' +
                '    }\n' +
                '}',
            wide : true,
            type : 'Snippet'
        }]))
        .add('snippet with bullet points', createPresentationDemo([{
            lang : 'java',
            type : 'Snippet',
            snippet : javaCodeWithMultilineComments(),
            commentsType: 'inline'
        }]))
        .add('long snippet with bullet points', createPresentationDemo([{
            lang : 'java',
            type : 'Snippet',
            snippet : longJavaCodeWithComment(),
            commentsType: 'inline'
        }]))
        .add('read more', createPresentationDemo([{
            type: 'Snippet',
            lang: 'java',
            readMore: true,
            snippet: longJavaCode(),
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

function longJavaCode() {
    let code = 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n'

    for (let idx = 0; idx < 60; idx++) {
        code += `        System.out.println("hello ${idx + 1}");\n`
    }

    code +=         '        ...\n' +
        '    }\n' +
        '}\n'

    return code
}

function longJavaCodeWithComment() {
    let code = 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n'

    for (let idx = 0; idx < 15; idx++) {
        if (idx === 10) {
            code += '        System.out.println("hello ' + (idx + 1) + '}"); // multiline comment multi line comment ' +
                'multiline comment multi line comment multiline comment multiline comment multi line comment multiline ' +
                'comment multi line comment multiline comment multiline comment multi line comment multiline comment ' +
                'multi line comment multiline comment multiline comment multi line comment multiline comment multi' +
                ' line comment multiline comment \n'
        } else if (idx === 12) {
            code += '        System.out.println("hello ' + (idx + 1) + '}"); // multiline comment multi line comment\n'
        } else {
            code += '        System.out.println("hello ' + (idx + 1) + '}");\n'

        }
    }

    code +=         '        ...\n' +
        '    }\n' +
        '}\n'

    return code
}

function javaCodeWithMultilineComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ... // multiline comment multi line comment multiline comment multi line comment multiline comment ' +
        'multi line comment multiline comment multi line comment multi line comment multiline comment multi line comment multi line comment multiline comment multi line comment \n' +
        '    } // Code stops here code stops here code stops here code' +
        ' stops here code stops here code stops here code stops here code stops here code stops here \n' +
        '}\n'
}


