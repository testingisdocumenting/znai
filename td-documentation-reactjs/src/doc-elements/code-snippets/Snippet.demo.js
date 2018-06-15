import React from 'react'

import {parseCode} from './codeParser'

import {Snippet} from '../default-elements/Snippet'
import {Tabs} from '../tabs/Tabs'

import {elementsLibrary} from '../DefaultElementsLibrary'

import {TwoSidesLayoutRightPart} from '../page/two-sides/TwoSidesLayout'

import './tokens.css'

export function snippetsDemo(registry) {
    const parsedJavaWide = parseCode("java", wideCode())
    const parsedWithComments = parseCode("java", codeWithComments())

    registry
        .add('title', <Snippet title="snippet title" tokens={parseCode("html", htmlCode())}/>)
        .add('wide with title', <Snippet wide={true} title="snippet title" tokens={parsedJavaWide}/>)
        .add('with bullet points', <Snippet wide={false} tokens={parsedWithComments} commentsType="inline"/>)
        .add('with spoiler bullet points', <Snippet wide={false} tokens={parsedWithComments} spoiler={true} commentsType="inline"/>)
        .add('wide with bullet points', <Snippet wide={true} tokens={parsedJavaWide} commentsType="inline"/>)
        .add('wide with bullet points right side background', <TwoSidesLayoutRightPart><Snippet wide={true} tokens={parsedJavaWide} commentsType="inline"/></TwoSidesLayoutRightPart>)
        .add('wide with spoiler bullet points', <Snippet wide={true} spoiler={true} tokens={parsedJavaWide} commentsType="inline"/>)
        .add('horizontal scroll', <Snippet wide={false} tokens={parsedJavaWide}/>)
        .add('highlight by line idx', <Snippet tokens={parseCode("markdown", markdownCode())} highlight={[0]}/>)
        .add('highlight by text', <Snippet tokens={parseCode("markdown", markdownCode())} highlight={"include-file"}/>)
        .add('highlight by text right side background', <TwoSidesLayoutRightPart><Snippet tokens={parsedJavaWide} highlight={"createMegaAbstractFactory"}/></TwoSidesLayoutRightPart>)
        .add('read more', <Snippet tokens={parseCode("csv", longCode())}
                                   readMore={true} r
                                   eadMoreVisibleLines={4}/>)
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
                maxLineLength: 200,
                tokens: parseCode("java", wideCode())
            }]}]}
}