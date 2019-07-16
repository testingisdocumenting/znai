import React from 'react'
import Xml from './Xml'
import {createPresentationDemo} from '../demo-utils/PresentationDemo'

export function xmlPresentationDemo(registry) {
    registry
        .add('xml presentation', createPresentationDemo([{
            type: 'Xml',
            xmlAsJson: nestedWithMultipleAttrs(),
            paths: ['ul.li[0]', 'ul.li[1].@class', 'ul.li[1].b']
        }]))
}

function nestedWithMultipleAttrs() {
    return {
        "tagName": "ul",
        "attributes": [{"name": "firstName", "value": "{this.firstName}"}, {
            "name": "lastName",
            "value": "{this.lastName}"
        }],
        "children": [
            {
                "tagName": "li",
                "attributes": [{"name": "class", "value": "\"menu-item\""}],
                "children": [
                    {
                        "tagName": "",
                        "text": "free form text"
                    }
                ]
            },
            {
                "tagName": "li",
                "attributes": [{"name": "class", "value": "\"menu-item\""}],
                "children": [
                    {
                        "tagName": "",
                        "text": "free"
                    },
                    {
                        "tagName": "b",
                        children: [
                            {
                                "tagName": "",
                                "text": "form"
                            }
                        ]
                    },
                    {
                        "tagName": "",
                        "text": "text"
                    }
                ]
            }
        ]
    }
}