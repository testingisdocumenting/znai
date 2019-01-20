import React from 'react'
import {Tabs} from './Tabs'
import {elementsLibrary} from '../DefaultElementsLibrary'

export function tabsDemo(registry) {
    registry
        .add('with code', () => <Tabs tabsContent={shortTabsContent()} elementsLibrary={elementsLibrary}/>, '')
        .add('tabs to test switch 1', () => <Tabs tabsContent={contentToSwitch()}
                                                  elementsLibrary={elementsLibrary}/>, '')
        .add('tabs to test switch 2', () => <Tabs tabsContent={contentToSwitch()}
                                                  elementsLibrary={elementsLibrary}/>, '')
        .add('tabs to test switch 3', () => <Tabs tabsContent={contentToSwitch()}
                                                  elementsLibrary={elementsLibrary}/>, '')
        .add('tabs to test switch 4', () => <Tabs tabsContent={contentToSwitch()}
                                                  elementsLibrary={elementsLibrary}/>, '')
}

function shortTabsContent() {
    return [
        {
            "name": "cpp",
            "content": [
                {
                    "lang": "cpp",
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
}

function contentToSwitch() {
    return [
        {
            "name": "small",
            "content": genParagraphs(5)
        },
        {
            "name": "big",
            "content": genParagraphs(20)
        }
    ]

}

function genParagraphs(number) {
    let result = []
    for (let idx = 0; idx < number; idx++) {
        result.push({
                "type": "Paragraph",
                "content": [
                    {
                        "text": "line number " + (idx + 1),
                        "type": "SimpleText"
                    }
                ]
            }
        )
    }

    return result
}