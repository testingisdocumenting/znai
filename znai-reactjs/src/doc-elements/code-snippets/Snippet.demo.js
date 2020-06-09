/*
 * Copyright 2020 znai maintainers
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
        .add('with linked method calls', () => <Snippet wide={true} title="snippet title" lang="java"
                                                        references={methodCallReferences()}
                                                        snippet={codeWithMethodCalls()}/>)
        .add('with bullet points java', () => <Snippet wide={false} lang="java" snippet={javaCodeWithComments()}
                                                       commentsType="inline"/>)
        .add('with bullet points python', () => <Snippet wide={false} lang="python" snippet={pythonCodeWithComments()}
                                                         commentsType="inline"/>)
        .add('with spoiler bullet points', () => <Snippet wide={false} lang="java" snippet={javaCodeWithComments()}
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
        .add('horizontal scroll with title and highlight', () => <Snippet wide={false} lang="java" snippet={wideCode()}
                                                                          highlight={2} title="Hello Snippet"/>)
        .add('highlight by line idx', () => <Snippet lang="markdown" snippet={markdownCode()} highlight={[0]}/>)
        .add('highlight by text', () => <Snippet lang="markdown" snippet={markdownCode()} highlight={"include-file"}/>)
        .add('highlight by text right side background', () => <TwoSidesLayoutRightPart><Snippet lang="java"
                                                                                                snippet={wideCode()}
                                                                                                highlight={"createMegaAbstractFactory"}/></TwoSidesLayoutRightPart>)
        .add('read more', () => <Snippet lang="csv" snippet={longCode()}
                                         readMore={true} readMoreVisibleLines={4}/>)
        .add('tabs with wide', () => <Tabs {...tabsContent({label: 'wide', wide: true})}
                                           elementsLibrary={elementsLibrary}/>)
        .add('large java code with javadocs', () => <Snippet lang="java" snippet={largeJavaCodeWithJavaDocs()}/>)
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

function javaCodeWithComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ... // code goes here\n' +
        '    } // code stops here\n' +
        '}\n'
}

function pythonCodeWithComments() {
    return 'def method:\n' +
        '    print("hello") # hello message\n'
}

function codeWithoutComments() {
    return 'class InternationalPriceService implements PriceService {\n' +
        '    private static void main(String... args) {\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
}

function codeWithMethodCalls() {
    return 'http.get("/end-point", http.header("h1", "v1"), ((header, body) -> {\n' +
        '    body.get("price").should(equal(100));\n' +
        '}));'
}

function methodCallReferences() {
    return {
        'http.header': {
            pageUrl: '#http-header'
        },
        'body.get': {
            pageUrl: '/chapter/page'
        },
        'should': {
            pageUrl: 'http://example.com'
        }
    }
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
        "snippet": javaCodeWithComments(),
        "meta": {
            "rightSide": isRightSide
        }
    }
}

function largeJavaCodeWithJavaDocs() {
    return "/**\n" +
        " * Top level conceptual description of a <i>Domain</i> problem.\n" +
        " * <p>\n" +
        " * To avoid <b>copy & paste</b> of the content consider to re-use information.\n" +
        " */\n" +
        "class HelloWorld {\n" +
        "    /**\n" +
        "     * Each year we hire students from different universities to increase\n" +
        "     * <code>diversity</code>\n" +
        "     */\n" +
        "    private int numberOfStudents;\n" +
        "\n" +
        "    /**\n" +
        "     * Conceptual description of a <i>Domain</i> problem.\n" +
        "     * <p>\n" +
        "     * It will work only if you put high level description here and\n" +
        "     * <b>not</b> implementation details.\n" +
        "     *\n" +
        "     * @param p1 important parameter of something\n" +
        "     * @param p2 sample offset according to the rules of the universe\n" +
        "     * @return name of the best sample\n" +
        "     */\n" +
        "    public String sampleMethod(String p1, int p2) {\n" +
        "        validate();\n" +
        "        process(p2); // important comment\n" +
        "        notifyAll(p1); // very important\n" +
        "\n" +
        "        return bestSample();\n" +
        "    }\n" +
        "\n" +
        "    public void sampleMethod(Map<String, Integer> p1, int p2, boolean isActive) {\n" +
        "        // overloaded method\n" +
        "    }\n" +
        "\n" +
        "    public void importantAction() {\n" +
        "        // TODO important\n" +
        "    }\n" +
        "\n" +
        "    public Data createData() {\n" +
        "        // create data\n" +
        "    }\n" +
        "}"
}