/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        .add('no attributes', () => <JsxGroup declarations={[noAttrs]}/>)
        .add('single attribute', () => <JsxGroup declarations={[singleAttr]}/>)
        .add('multiple attributes', () => <JsxGroup declarations={[multipleAttrs]}/>)
        .add('multiple entries', () => <JsxGroup declarations={[singleAttr, multipleAttrs]}/>)
}
