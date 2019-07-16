import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'

const content = [{
    "bulletMarker": "*",
    "tight": true,
    "type": "BulletList",
    "content": [
        {
            "type": "ListItem",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [
                        {
                            "type": "Icon",
                            "id": "search"
                        },
                        {
                            "text": "Something",
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
                            "type": "Icon",
                            "id": "time"
                        },
                        {
                            "text": "Additional Additional Additional Additional Additional Additional Additional Additional Additional Additional Additional Additional",
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
                            "type": "Icon",
                            "id": "thumbs-down"
                        },
                        {
                            "type": "Emphasis",
                            "content": [
                                {
                                    "text": "Inject",
                                    "type": "SimpleText"
                                }
                            ]
                        },
                        {
                            "text": " ",
                            "type": "SimpleText"
                        },
                        {
                            "code": "DAO",
                            "type": "InlinedCode"
                        },
                        {
                            "text": " to decouple",
                            "type": "SimpleText"
                        }
                    ]
                }
            ]
        }
    ]
}];

const IconsAsBulletsDemo = () => {
    return <elementsLibrary.DocElement content={content} elementsLibrary={elementsLibrary}/>
}

export default IconsAsBulletsDemo