import React from 'react'
import Tabs from './Tabs'

const tabsContent = [{name: 'Java', content: [{
    "lang": "javascript",
    "maxLineLength": 38,
    "tokens": [
        {
            "type": "keyword",
            "data": "import"
        },
        {
            "type": "text",
            "data": " React"
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "punctuation",
            "data": "{"
        },
        {
            "type": "text",
            "data": "Component"
        },
        {
            "type": "punctuation",
            "data": "}"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "keyword",
            "data": "from"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "string",
            "data": "\u0027"
        },
        {
            "type": "text",
            "data": "react"
        },
        {
            "type": "string",
            "data": "\u0027"
        },
        {
            "type": "text",
            "data": "\n\n"
        },
        {
            "type": "keyword",
            "data": "class"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "class-name",
            "data": "MyComponent"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "keyword",
            "data": "extends"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "class-name",
            "data": "Component"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "punctuation",
            "data": "{"
        },
        {
            "type": "text",
            "data": "\n    "
        },
        {
            "type": "function",
            "data": "render"
        },
        {
            "type": "punctuation",
            "data": "("
        },
        {
            "type": "punctuation",
            "data": ")"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "punctuation",
            "data": "{"
        },
        {
            "type": "text",
            "data": "\n        "
        },
        {
            "type": "comment",
            "data": "/// ..."
        },
        {
            "type": "text",
            "data": "\n    "
        },
        {
            "type": "punctuation",
            "data": "}"
        },
        {
            "type": "text",
            "data": "\n"
        },
        {
            "type": "punctuation",
            "data": "}"
        },
        {
            "type": "text",
            "data": "\n"
        }
    ],
    "lineNumber": "",
    "type": "Snippet"}]},
    {name: 'Python', content: [{
        "lang": "javascript",
        "maxLineLength": 98,
        "tokens": [
            {
                "type": "keyword",
                "data": "class"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "class-name",
                "data": "InternationalPriceService"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "keyword",
                "data": "implements"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "class-name",
                "data": "PriceService"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "punctuation",
                "data": "{"
            },
            {
                "type": "text",
                "data": "\n    "
            },
            {
                "type": "keyword",
                "data": "private"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "keyword",
                "data": "static"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "keyword",
                "data": "void"
            },
            {
                "type": "text",
                "data": " LongJavaInterfaceNameWithSuperFactory "
            },
            {
                "type": "function",
                "data": "createMegaFactory"
            },
            {
                "type": "punctuation",
                "data": "("
            },
            {
                "type": "keyword",
                "data": "final"
            },
            {
                "type": "text",
                "data": " ExchangeCalendar calendar"
            },
            {
                "type": "punctuation",
                "data": ")"
            },
            {
                "type": "text",
                "data": " "
            },
            {
                "type": "punctuation",
                "data": "{"
            },
            {
                "type": "text",
                "data": "\n        "
            },
            {
                "type": "punctuation",
                "data": "."
            },
            {
                "type": "punctuation",
                "data": "."
            },
            {
                "type": "punctuation",
                "data": "."
            },
            {
                "type": "text",
                "data": "\n    "
            },
            {
                "type": "punctuation",
                "data": "}"
            },
            {
                "type": "text",
                "data": "\n"
            },
            {
                "type": "punctuation",
                "data": "}"
            },
            {
                "type": "text",
                "data": "\n"
            }
        ],
        "lineNumber": "",
        "type": "Snippet"
    }]}]

const TabsDemo = () => {
    return (<Tabs tabsContent={tabsContent}/>)
}

export default TabsDemo