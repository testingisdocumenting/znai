import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'
import Table from './Table'

export function tableDemo(registry) {
    registry
        .add('no style', <Table table={defaultStyle(twoColumnsData())} elementsLibrary={elementsLibrary}/>)
        .add('no header, vertical only', <Table table={vertLinesOnly(twoColumnsData())}
                                                elementsLibrary={elementsLibrary}/>)
        .add('no header, vertical only, no vertical padding', <Table table={noVertPadding(twoColumnsData())}
                                                                     elementsLibrary={elementsLibrary}/>)
        .add('long inlined code', <Table table={defaultStyle(dataWithLongInlinedCode())}
                                         elementsLibrary={elementsLibrary}/>)
        .add('with code snippet', <Table table={defaultStyle(dataWithCodeSnippet())}
                                         elementsLibrary={elementsLibrary}/>)
}

function defaultStyle(data) {
    return {
        styles: [],
        columns: twoColumns(),
        data: data
    }
}

function vertLinesOnly(data) {
    return {
        styles: ['no-header', 'middle-vertical-lines-only'],
        columns: twoColumns(),
        data: data
    }
}

function noVertPadding(data) {
    return {
        styles: ['no-header', 'middle-vertical-lines-only', 'no-vertical-padding'],
        columns: twoColumns(),
        data: data
    }
}

function twoColumns() {
    return [
        {title: 'Column 1', align: 'right'},
        {title: 'Column 2', width: '50%'}
    ]
}

function twoColumnsData() {
    return [
        [1, 2],
        [3, 4],
        ['hello', [
            {
                'text': 'We saw (todo link) how you can annotate images using ',
                'type': 'SimpleText'
            },
            {
                'code': 'include-image',
                'type': 'InlinedCode'
            },
            {
                'text': ' plugin.',
                'type': 'SimpleText'
            },
            {
                'type': 'SoftLineBreak'
            },
            {
                'text': 'Now let\u0027s automate the screenshot and annotations assigning process.',
                'type': 'SimpleText'
            }
        ]],
    ]
}

function dataWithLongInlinedCode() {
    return [
        [1, 2],
        ['hello', [
            {
                'text': 'We saw (todo link) how you can annotate images using ',
                'type': 'SimpleText'
            },
            {
                'code': 'longAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreAlongAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreAlongAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreAlongAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreA',
                'type': 'InlinedCode'
            },
        ]],
    ]
}

function dataWithCodeSnippet() {
    return [
        [1, 2],
        ['hello', [
            {
                'snippet': 'class InternationalPriceService implements PriceService, AnotherInterface {\n' +
                '    private static void main(String... args) {\n' +
                '        ... // code goes here\n' +
                '    } // code stops here\n' +
                '}\n',
                'lang': 'Java',
                'type': 'Snippet',
                'title': 'Snippet in a cell'
            }
        ]],
    ]
}