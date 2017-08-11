import React from 'react'
import {BulletList} from './BulletList'

import {elementsLibrary} from '../DefaultElementsLibrary'

const content = [
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
                                "text": "Risk management",
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
    }];

const BulletListDemo = () => {
    return <BulletList content={content} elementsLibrary={elementsLibrary} isPresentation={true}/>
}

export default BulletListDemo