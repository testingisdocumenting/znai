import React from 'react'
import Xml from './Xml'

export function xmlDemo(registry) {
    registry
        .add('no attributes', <Xml xmlAsJson={noAttrs()}/>)
        .add('single attribute', <Xml xmlAsJson={singleAttr()}/>)
        .add('multiple attributes', <Xml xmlAsJson={multipleAttrs()}/>)
        .add('multiple attributes with highlights', <Xml xmlAsJson={multipleAttrs()} paths={['Component.@lastName']}/>)
        .add('nested', <Xml xmlAsJson={nested()}/>)
        .add('nested with multiple highlights', <Xml xmlAsJson={nested()} paths={['ul.li[0]', 'ul.li[1].@class', 'ul.li[1].b']}/>)
        .add('nested with multiple attributes', <Xml xmlAsJson={nestedWithMultipleAttrs()}/>)
}

function noAttrs() {
    return {
        "tagName": "div",
        "attributes": []
    }
}

function singleAttr() {
    return {
        "tagName": "div",
        "attributes": [{"name": "class", "value": "{this.firstName}"}]
    }
}

function multipleAttrs() {
    return {
        "tagName": "Component",
        "attributes": [
            {"name": "firstName", "value": "{this.firstName}"},
            {"name": "lastName", "value": "{this.lastName}"}
        ]
    }
}

function nested() {
    return {
        "tagName": "ul",
        "attributes": [],
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