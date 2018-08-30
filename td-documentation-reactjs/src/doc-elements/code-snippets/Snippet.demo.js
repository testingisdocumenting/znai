import React from 'react'

import {Snippet} from '../default-elements/Snippet'
import {Tabs} from '../tabs/Tabs'

import {elementsLibrary} from '../DefaultElementsLibrary'

import {TwoSidesLayoutRightPart} from '../page/two-sides/TwoSidesLayout'

import './tokens.css'

export function snippetsDemo(registry) {
    registry
        .add('title', <Snippet title="snippet title" lang="html" snippet={htmlCode()}/>)
        .add('wide with title', <Snippet wide={true} title="snippet title" lang="java" snippet={wideCode()}/>)
        .add('with bullet points', <Snippet wide={false} lang="java" snippet={codeWithComments()} commentsType="inline"/>)
        .add('with spoiler bullet points', <Snippet wide={false} lang="java" snippet={codeWithComments()}  spoiler={true} commentsType="inline"/>)
        .add('wide with bullet points', <Snippet wide={true} lang="java" snippet={wideCode()} commentsType="inline"/>)
        .add('wide with bullet points right side background', <TwoSidesLayoutRightPart><Snippet wide={true} lang="java" snippet={wideCode()} commentsType="inline"/></TwoSidesLayoutRightPart>)
        .add('wide with spoiler bullet points', <Snippet wide={true} spoiler={true} lang="java" snippet={wideCode()} commentsType="inline"/>)
        .add('horizontal scroll', <Snippet wide={false} lang="java" snippet={wideCode()}/>)
        .add('highlight by line idx', <Snippet lang="markdown" snippet={markdownCode()} highlight={[0]}/>)
        .add('highlight by text', <Snippet lang="markdown" snippet={markdownCode()} highlight={"include-file"}/>)
        .add('highlight by text right side background', <TwoSidesLayoutRightPart><Snippet lang="java" snippet={wideCode()} highlight={"createMegaAbstractFactory"}/></TwoSidesLayoutRightPart>)
        .add('read more', <Snippet lang="csv" snippet={longCode()}
                                   readMore={true} readMoreVisibleLines={4}/>)
        .add('tabs with wide', <Tabs {...tabsContent({label: 'wide', wide: true})}
                                     elementsLibrary={elementsLibrary}/>)
        .add('tabs with narrow', <Tabs {...tabsContent({label: 'narrow', wide: false})}
                                       elementsLibrary={elementsLibrary}/>)
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
    return {tabsContent: [{
            name: label, content: [{
                type: "Snippet",
                wide: wide,
                lang: "java",
                snippet: wideCode()
            }]}]}
}