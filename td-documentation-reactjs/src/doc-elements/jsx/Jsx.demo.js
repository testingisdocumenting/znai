import React from 'react'
import JsxGroup from './JsxGroup'

const noAttrs = {
    "tagName": "Declaration",
    "attributes": []
}

const singleAttr = {
    "tagName": "Declaration",
    "attributes": [{"name": "firstName", "value": "{this.firstName}"}]
}

const multipleAttrs = {
    "tagName": "Declaration",
    "attributes": [{"name": "firstName", "value": "{this.firstName}"}, {
        "name": "lastName",
        "value": "{this.lastName}"
    }]
}

export function jsxDemo(registry) {
    registry
        .add('no attributes', <JsxGroup declarations={[noAttrs]}/>)
        .add('single attribute', <JsxGroup declarations={[singleAttr]}/>)
        .add('multiple attributes', <JsxGroup declarations={[multipleAttrs]}/>)
        .add('multiple entries', <JsxGroup declarations={[singleAttr, multipleAttrs]}/>)
}
