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

import {Snippet} from './Snippet'
import {Tabs} from '../tabs/Tabs'

import {elementsLibrary} from '../DefaultElementsLibrary'

import {TwoSidesLayoutRightPart} from '../page/two-sides/TwoSidesLayout'

import {Page} from '../page/Page'
import {Section} from '../default-elements/Section';

import { simulateState } from "react-component-viewer";

import ApiParameters from "../api/ApiParameters";

import {
    codeWithMethodCalls,
    contentParagraph,
    contentSnippet,
    personApiParameters
} from "../demo-utils/contentGenerators";

import './tokens.css'

const [getReadMore, setReadMore] = simulateState(true);

export function snippetsDemo(registry) {
    registry
      .add('title', () => <Snippet title="snippet title" lang="html" snippet={htmlCode()}/>)
      .add('title collapsible', () => <Snippet title="snippet title" lang="html" snippet={htmlCode()} collapsed={false}/>)
      .add('title no gap', () => (
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[
            contentParagraph(false),
            compactContentSnippet("snippet one", {noGap: true}),
            compactContentSnippet("snippet two"),
            compactContentSnippet("snippet three", {noGap: true}),
            compactContentSnippet("snippet four"),
            contentParagraph(false),
        ]}/>
      ))
      .add('separator no gap border', () => (
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[
            contentParagraph(false),
            compactContentSnippet("examples", {noGap: true, noGapBorder: true}),
            compactContentSnippet(undefined),
            contentParagraph(false),
        ]}/>
      ))
      .add('collapsible next to api params', () => (<>
          <ApiParameters elementsLibrary={elementsLibrary} parameters={personApiParameters} title="Person definition" collapsed={false}/>
          <Snippet title="snippet title" lang="html" snippet={htmlCode()} collapsed={false}/>
      </>))
      .add('title anchor', () => <Snippet title="snippet title" anchorId="my-code" lang="html" snippet={htmlCode()}/>)
      .add('wide with title', () => <Snippet wide={true} title="snippet title" lang="java" snippet={wideCode()}/>)
      .add('wide with title and anchor', () => <Snippet wide={true} title="snippet title" lang="java" anchorId="wide-code-anchor" snippet={wideCode()}/>)
      .add('wide with title collapsible', () => <Snippet wide={true} title="snippet title" lang="java" snippet={wideCode()} collapsed={false}/>)
      .add('with linked method calls', () => <Snippet wide={true} title="snippet title" lang="java"
                                                      references={methodCallReferences()}
                                                      snippet={codeWithMethodCalls()}/>)
      .add('horizontal scroll', () => <Snippet wide={false} lang="java" snippet={wideCode()}/>)
      .add('wrap', () => <Snippet wide={false} lang="java" snippet={wideCode()} wrap={true}/>)
      .add('horizontal scroll with title and highlight', () => <Snippet wide={false} lang="java" snippet={wideCode()}
                                                                        highlight={2} title="Hello Snippet"/>)
      .add('highlight by line idx', () => <Snippet lang="markdown" snippet={markdownCode()} highlight={[0]}/>)
      .add('highlight right side background', () => <TwoSidesLayoutRightPart><Snippet lang="java"
                                                                                      snippet={wideCode()}
                                                                                      highlight={[1]}/></TwoSidesLayoutRightPart>)
      .add('read more', () => <Snippet lang="csv" snippet={longCode()}
                                       readMore={true} readMoreVisibleLines={4}/>)
      .add('read more switch', () => (
        <div>
            <button onClick={() => setReadMore(!getReadMore())}>toggle read more</button>
            <Snippet lang="csv" snippet={longCode()}
                     readMore={getReadMore()} readMoreVisibleLines={4}/>)
        </div>))
      .add('tabs with wide', () => <Tabs {...tabsContent({label: 'wide', wide: true})}
                                         elementsLibrary={elementsLibrary}/>)
      .add('python code', () => <Snippet lang="python" snippet={pythonCode()}/>)
      .add('large java code with javadocs', () => <Snippet lang="java" highlight={[0, 3, 6]} snippet={largeJavaCodeWithJavaDocs()}/>)
      .add('markdown code', () => <Snippet lang="markdown" highlight={[0, 2]} snippet={markdownCode()}/>)
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

export function snippetsTwoColumnsDemo(registry) {
    const commonProps = {
        elementsLibrary,
        docMeta: docMeta(),
        title: "Section title",
        id: "section-title"
    }
    registry
      .add('before snippet', () => <Section {...commonProps}
                                            content={beforeSnippetContent()}/>)
        .add('after snippet', () => <Section {...commonProps}
                                             content={afterSnippetContent()}/>)
      .add('surrounded by text', () => <Section {...commonProps}
                                        content={surroundedByTextContent()}/>)

    function beforeSnippetContent() {
        return [
            {
                type: "Columns",
                config: {},
                columns: [
                    {content: [contentSnippet(false)]},
                    {content: [contentSnippet(false)]}
                ]
            },
            contentSnippet(false),
        ]
    }

    function afterSnippetContent() {
        return [
            contentSnippet(false),
            {
                type: "Columns",
                config: {},
                columns: [
                    {content: [contentSnippet(false)]},
                    {content: [contentSnippet(false)]}
                ]
            }
        ]
    }

    function surroundedByTextContent() {
        return [
            contentParagraph(false),
            {
                type: "Columns",
                config: {},
                columns: [
                    {content: [contentSnippet(false)]},
                    {content: [contentSnippet(false)]}
                ]
            },
            contentParagraph(false),
        ]
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
    return 'class InternationalPriceService implements PriceService { // classic\n' +
        '    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) {\n' +
        '        ...\n' +
        '    }\n' +
        '}\n'
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
        ':include-file: config/server.config\n\nNote: hello world\n'
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
    return pageContent("two-sides", content)
}

function pageContent(type, content) {
    return {
        "type": "Page",
        "content": content,
        "lastModifiedTime": 1527473295000,
        "tocItem": {
            "sectionTitle": "Demo",
            "pageTitle": "Demo",
            "pageMeta": {
                "type": [
                    type
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

function compactContentSnippet(title, {noGap, noGapBorder} = {}) {
    return {
        "type": "Snippet",
        "lang": "java",
        "title": title,
        "noGap": noGap,
        "noGapBorder": noGapBorder,
        "snippet": codeWithMethodCalls(),
    }
}

function pythonCode() {
    return "import market\n" +
        "import flight\n" +
        "\n" +
        "def main():\n" +
        "    id = market.book_trade('symbol', market.CURRENT_PRICE, 100)\n" +
        "\n" +
        "    market.cancel_trade('id')\n" +
        "\n" +
        "if __name__  == \"__main__\":\n" +
        "    main() "
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