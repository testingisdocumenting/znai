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

const TabsDemo = () => {
    return (<Tabs tabsContent={tabsContent} elementsLibrary={elementsLibrary}/>)
}

export default TabsDemo