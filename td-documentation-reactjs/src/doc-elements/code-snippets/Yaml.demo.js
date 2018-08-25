import React from 'react'

import {parseCode} from './codeParser'
import {Snippet} from '../default-elements/Snippet'

import './tokens.css'

export function yamlSnippetDemo(registry) {
    const parsed = parseCode("yaml", yamlCode())

    registry
        .add('highlight by line idx', <Snippet tokens={parsed} highlight={[3, 5]}/>)
        .add('highlight by text', <Snippet tokens={parsed} highlight={"family"}/>)
}

function yamlCode() {
    return 'invoice: 34843\n' +
        'date   : 2001-01-23\n' +
        'bill-to: &id001\n' +
        '    given  : Chris\n' +
        '    family : Dumars\n' +
        '    address:\n' +
        '        lines: |\n' +
        '            458 Walkman Dr.\n' +
        '            Suite #292\n' +
        '        city    : Royal Oak\n' +
        '        state   : MI\n' +
        '        postal  : 48046\n' +
        'comments: >\n' +
        '    Late afternoon is best.\n' +
        '    Backup contact is Nancy\n' +
        '    Billsmer @ 338-4338.'
}
