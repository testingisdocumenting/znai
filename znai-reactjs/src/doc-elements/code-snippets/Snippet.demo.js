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

import React from 'react'

import {Snippet} from '../default-elements/Snippet'
import {Tabs} from '../tabs/Tabs'

import {elementsLibrary} from '../DefaultElementsLibrary'

import {TwoSidesLayoutRightPart} from '../page/two-sides/TwoSidesLayout'

import {Page} from '../page/Page'

import './tokens.css'

export function snippetsDemo(registry) {
    registry
        .add('title', () => <Snippet title="snippet title" lang="html" snippet={htmlCode()}/>)
        .add('wide with title', () => <Snippet wide={true} title="snippet title" lang="java" snippet={wideCode()}/>)
        .add('with bullet points', () => <Snippet wide={false} lang="java" snippet={codeWithComments()}
                                                  commentsType="inline"/>)
        .add('with spoiler bullet points', () => <Snippet wide={false} lang="java" snippet={codeWithComments()}
                                                          spoiler={true}
                                                          commentsType="inline"/>)
        .add('wide with bullet points', () => <Snippet wide={true} lang="java" snippet={wideCode()}
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
        .add('horizontal scroll', () => <Snippet wide={false} lang="java" snippet={wideCode()}/>)
        .add('highlight by line idx', () => <Snippet lang="markdown" snippet={markdownCode()} highlight={[0]}/>)
        .add('highlight by text', () => <Snippet lang="markdown" snippet={markdownCode()} highlight={"include-file"}/>)
        .add('highlight by text right side background', () => <TwoSidesLayoutRightPart><Snippet lang="java"
                                                                                                snippet={wideCode()}
                                                                                                highlight={"createMegaAbstractFactory"}/></TwoSidesLayoutRightPart>)
        .add('read more', () => <Snippet lang="csv" snippet={longCode()}
                                         readMore={true} readMoreVisibleLines={4}/>)
        .add('tabs with wide', () => <Tabs {...tabsContent({label: 'wide', wide: true})}
                                           elementsLibrary={elementsLibrary}/>)
}

export function snippetsTwoSidesDemo(registry) {
    registry
        .add('code after heading', () => <Page elementsLibrary={elementsLibrary}
                                               docMeta={docMeta()}
                                               {...twoSidesPage(snippetAfterSection())}/>)
        .add('text between code blocks', () => <Page elementsLibrary={elementsLibrary}
                                                     docMeta={docMeta()}
                                                     {...twoSidesPage(textBetweenSnippetsMultipleBlocks())}/>)
}

function docMeta() {
    return {
        "title": "Doc Title",
        "type": "Guide"
    }
}

function htmlCode() {
    return '<div id="menu">\n' +
        '  <ul>\n' +
        '   <li> <a href="/book">book</a> </li>\n' +
        '   <li> <a href="/orders">orders</a> </li>\n' +
        '   <li> <a href="/help">help</a> </li>\n' +
        ' </ul>\n' +
        '</div>\n'
}

function wideCode() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {\n' +
        '        ... // code goes here\n' +
        '    } // code stops here\n' +
        '}\n'
}

function codeWithComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ... // code goes here\n' +
        '    } // code stops here\n' +
        '}\n'
}

function codeWithoutComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
}

function markdownCode() {
    return '# Server Configuration\n\n' +
        ':include-file: config/server.config\n'
}

function longCode() {
    let lines = []
    for (let i = 1; i <= 30; i++) {
        lines.push('line of text number ' + i)
    }

    return lines.join('\n')
}

function tabsContent({label, wide}) {
    return {
        tabsContent: [{
            name: label, content: [{
                type: "Snippet",
                wide: wide,
                lang: "java",
                snippet: wideCode()
            }]
        }]
    }
}

function twoSidesPage(content) {
    return {
        "type": "Page",
        "content": content,
        "lastModifiedTime": 1527473295000,
        "tocItem": {
            "sectionTitle": "Demo",
            "pageTitle": "Demo",
            "pageMeta": {
                "type": [
                    "two-sides"
                ]
            },
            "fileName": "demo",
            "dirName": "demo",
            "pageSectionIdTitles": []
        }
    }
}

function snippetAfterSection() {
    return [
        {
            "title": "Primary Use Case",
            "id": "primary-use-case",
            "type": "Section",
            "content": [contentSnippet(false)]
        }
    ]
}

function textBetweenSnippetsMultipleBlocks() {
    return [
        {
            "title": "Primary Use Case",
            "id": "primary-use-case",
            "type": "Section",
            "content": [
                contentSnippet(false),
                contentSnippet(true),
                contentParagraph(false),
                contentParagraph(true),
                contentSnippet(false),
            ]
        }
    ]
}

function contentParagraph(isRightSide) {
    return {
        "type": "Paragraph",
        "content": [
            {
                "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs.",
                "type": "SimpleText"
            }
        ],
        "meta": {
            "rightSide": isRightSide
        }
    }
}

function contentSnippet(isRightSide) {
    return {
        "type": "Snippet",
        "lang": "java",
        "snippet": codeWithComments(),
        "meta": {
            "rightSide": isRightSide
        }
    }
}