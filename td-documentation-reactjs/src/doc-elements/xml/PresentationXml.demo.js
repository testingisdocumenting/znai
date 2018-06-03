import React from 'react'
import Xml from './Xml'
import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'
import PresentationRegistry from '../presentation/PresentationRegistry'
import Presentation from '../presentation/Presentation'

const docMeta = {id: "mdoc", title: "MDoc", type: "User Guide"}
const presentationRegistry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [{
    type: 'Xml',
    xmlAsJson: nestedWithMultipleAttrs(),
    paths: ['ul.li[0]', 'ul.li[1].@class', 'ul.li[1].b']
}])

export function xmlPresentationDemo(registry) {
    registry
        .add('xml presentation', <Presentation docMeta={docMeta} presentationRegistry={presentationRegistry}/>)
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