import React from 'react'

import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'

import Presentation from './Presentation'
import PresentationRegistry from './PresentationRegistry'

import '../DocumentationLayout.css'

const columnsContent = [
    {
        "columns": [
            {
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "type": "StrongEmphasis",
                                "content": [
                                    {
                                        "text": "Argument Name",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Argument description and what argument is for",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ],
        "config": {
            "left": {
                "width": 160.0
            },
            "border": true
        },
        "type": "Columns"
    }]

const quoteContent = [
    {
        "type": "Paragraph",
        "content": [
            {
                "content": [{"text": "test etest", type: "SimpleText"}],
                "type": "BlockQuote"
            },
        ]
    },
    {
        "type": "Paragraph",
        "content": [
            {
                "content": [{"text": "long long block quote. long long block quote. long long block quote. long long block quote. long long block quote. long long block quote", type: "SimpleText"}],
                "type": "BlockQuote"
            },
        ]
    }
]

const svgContent = [{
    "type": "Svg",
    "idsToReveal": ["part1", "part2", "part3"],
    "svg": "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
        "<!-- Generator: Adobe Illustrator 16.0.0, SVG Export Plug-In . SVG Version: 6.00 Build 0)  -->\n" +
        "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
        "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
        "\t width=\"978px\" height=\"978px\" viewBox=\"0 0 978 978\" style=\"enable-background:new 0 0 978 978;\" xml:space=\"preserve\">\n" +
        "<g id=\"part2\">\n" +
        "\t<path d=\"M229.2,463c68.8,26.9,161,41.7,259.8,41.7s191-14.8,259.8-41.7c65.101-25.4,88.2-53.6,88.2-70V269\n" +
        "\t\tc-20.6,14.1-44.9,26.7-72.8,37.6C689.4,335.9,591.7,352,489,352s-200.4-16.1-275.2-45.3c-27.9-10.9-52.3-23.5-72.8-37.6v124\n" +
        "\t\tC141,409.5,164.1,437.6,229.2,463z\"/>\n" +
        "</g>\n" +
        "<g id=\"part1\">\n" +
        "\t<path d=\"M229.2,697.2c27,10.6,57.6,19.2,90.8,25.899C371.4,733.4,429,738.9,489,738.9s117.6-5.5,169-15.801\n" +
        "\t\tc33.2-6.699,63.8-15.3,90.8-25.899c65.101-25.4,88.2-53.601,88.2-70V508.8c-17.1,10.5-36.9,20.101-59.1,28.8\n" +
        "\t\tC700.1,568,597.5,584.7,489,584.7S277.9,568,200.1,537.6c-22.2-8.699-41.9-18.3-59.1-28.8v118.5C141,643.6,164.1,671.7,229.2,697.2\n" +
        "\t\tz\"/>\n" +
        "</g>\n" +
        "<g id=\"part3\">\n" +
        "\t<path d=\"M489,978c192.2,0,348-60.9,348-136v-99.1c-15.7,9.6-33.6,18.5-53.6,26.6c-1.801,0.7-3.7,1.5-5.5,2.2\n" +
        "\t\tC700.1,802.1,597.5,818.9,489,818.9s-211.1-16.7-288.9-47.2c-1.9-0.7-3.7-1.5-5.5-2.2c-20-8.1-37.9-17-53.6-26.6V842\n" +
        "\t\tC141,917.1,296.8,978,489,978z\"/>\n" +
        "\t<ellipse cx=\"489\" cy=\"136\" rx=\"348\" ry=\"136\"/>\n" +
        "</g>\n" +
        "</svg>\n"

}]

const bulletContent = [
    {
        "bulletMarker": "*",
        "tight": true,
        "type": "BulletList",
        "meta": { "presentationBulletListType": "RevealBoxes", "differentColors": true },
        "content": [
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "users\u0027 time lost",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "reputation damage",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "cost of support",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "hidden burden",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ]
    }
]

const stepsBulletContent = [
    {
        "bulletMarker": "*",
        "tight": true,
        "type": "BulletList",
        "meta": { "bulletListType": "Steps", "differentColors": true },
        "content": [
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Something Additional Third Line",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Additional",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            },
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Third Step with text",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ]
    }]

const codeWithInlinedComments = [{
    "lang": "javascript",
    "maxLineLength": 52,
    "tokens": [
        {
            "type": "keyword",
            "content": "class"
        },
        " ",
        {
            "type": "class-name",
            "content": [
                "JsClass"
            ]
        },
        " ",
        {
            "type": "punctuation",
            "content": "{"
        },
        "\n    ",
        {
            "type": "function",
            "content": "constructor"
        },
        {
            "type": "punctuation",
            "content": "("
        },
        {
            "type": "punctuation",
            "content": ")"
        },
        " ",
        {
            "type": "punctuation",
            "content": "{"
        },
        " ",
        {
            "type": "comment",
            "content": "// new syntax for constructor"
        },
        "\n    ",
        {
            "type": "punctuation",
            "content": "}"
        },
        "\n",
        {
            "type": "punctuation",
            "content": "}"
        },
        "\n\n",
        {
            "type": "keyword",
            "content": "export"
        },
        " ",
        {
            "type": "keyword",
            "content": "default"
        },
        " JsClass ",
        {
            "type": "comment",
            "content": "// new syntax for ES6 modules"
        }
    ],
    "commentsType": "inline",
    "type": "Snippet"
}]

const cliCommand = {
    "type": "CliCommand",
    "command": "kubernetes install container --env=prod",
    "paramsToHighlight": ["env"]
}

const chart = {
    "type": "Chart",
    "innerRadius": 100.0,
    "chartType": "Bar",
    "data": [
        [
            "A",
            10
        ],
        [
            "B",
            20
        ],
        [
            "C",
            15
        ],
        [
            "D",
            8
        ]
    ]
}

const section1 = {
    title: "Section One",
    type: "Section",
    content: svgContent
}

const section2 = {
    title: "Section Two",
    type: "Section",
    content: codeWithInlinedComments
}

const page = {
    tocItem: {pageTitle: "Page Title"},
    type: "Page",
    content: columnsContent
}

const docMeta = {id: "mdoc", title: "MDoc", type: "User Guide"}

const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, bulletContent)
const PresentationDemo = (props) => <Presentation docMeta={docMeta} presentationRegistry={registry}/>

export default PresentationDemo
