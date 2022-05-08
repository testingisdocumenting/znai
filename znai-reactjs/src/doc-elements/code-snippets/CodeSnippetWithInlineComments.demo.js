/*
 * Copyright 2022 znai maintainers
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

import {Snippet} from "./Snippet";
import {TwoSidesLayoutRightPart} from "../page/two-sides/TwoSidesLayout";

export function snippetsWithInlineCommentsDemo(registry) {
    registry
        .add('with bullet points java', () => <Snippet wide={false} lang="java" snippet={javaWithComments()}
                                                       commentsType="inline"/>)
        .add('with multiline bullet points java', () => <Snippet wide={false} lang="java" snippet={javaCodeWithMultilineComments()}
                                                                 commentsType="inline"/>)
        .add('with bullet points python', () => <Snippet wide={false} lang="python" snippet={pythonCodeWithComments()}
                                                         commentsType="inline"/>)
        .add('with bullet points python comments above', () => <Snippet wide={false} lang="python" snippet={pythonCodeWithCommentsAbove()}
                                                                        commentsType="inline"/>)
        .add('with spoiler bullet points', () => <Snippet wide={false} lang="java" snippet={javaWithComments()}
                                                          spoiler={true}
                                                          commentsType="inline"/>)
        .add('wide with bullet points', () => <Snippet wide={true} lang="java" snippet={wideCode()}
                                                       commentsType="inline"/>)
        .add('wrap with bullet points', () => <Snippet wrap={true} lang="java" snippet={wideCode()}
                                                       commentsType="inline"/>)
        .add('wide with bullet points right side background', () => <TwoSidesLayoutRightPart><Snippet wide={true}
                                                                                                      lang="java"
                                                                                                      snippet={wideCode()}
                                                                                                      commentsType="inline"/></TwoSidesLayoutRightPart>)
        .add('wide with spoiler bullet points', () => <Snippet wide={true} spoiler={true} lang="java"
                                                               snippet={wideCode()}
                                                               commentsType="inline"/>)
        .add('with empty bullet points', () => <Snippet lang="java" snippet={codeWithoutComments()}
                                                        commentsType="inline"/>)

}

function javaWithComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ... // code goes here\n' +
        '    } // code stops here\n' +
        '}\n'
}

function wideCode() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) { // this one is long wow many text goes here still yeah\n' +
        '        ... // code goes here\n' +
        '    } // code stops here\n' +
        '}\n'
}

function javaCodeWithMultilineComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ... // multiline comment multi line comment multiline comment multi line comment multiline comment ' +
        'multi line comment multiline comment multi line comment \n' +
        '    } // Code stops here code stops here code stops here code' +
        ' stops here code stops here code stops here code stops here code stops here code stops here \n' +
        '}\n'
}

function pythonCodeWithComments() {
    return 'import market\n\n' +
        'def method:\n' +
        '    print("hello") # hello message\n'
}

function pythonCodeWithCommentsAbove() {
    return 'def method:\n' +
        '    # hello message\n' +
        '    # on multiple lines\n' +
        '    print("hello")\n'
}

function codeWithoutComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
}
