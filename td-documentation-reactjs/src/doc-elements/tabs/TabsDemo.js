import React from 'react'
import {Tabs} from './Tabs'
import {elementsLibrary} from '../DefaultElementsLibrary'

const tabsContent = [
    {
        "name": "cpp",
        "content": [
            {
                "lang": "cpp",
                "maxLineLength": 13,
                "tokens": [
                    "code snippet \n"
                ],
                "lineNumber": "",
                "type": "Snippet"
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "text after code snippet 2",
                        "type": "SimpleText"
                    }
                ]
            }
        ]
    }
]

export function tabsDemo(registry) {
    registry.add('with code', <Tabs tabsContent={tabsContent} elementsLibrary={elementsLibrary}/>, '')
}
