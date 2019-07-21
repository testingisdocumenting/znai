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

function svgAttributesToProps(attributes) {
    attributes = attributes || []
    let result = {}
    for (let i = 0, len = attributes.length; i < len; i++) {
        const item = attributes.item(i)
        if (item.name.indexOf(':') !== -1) {
            continue;
        }

        const name = convertAttrName(item.name)
        result[name] = convertAttrValue(name, item.nodeValue)
    }

    return result
}

function capitalize(name) {
    return name.charAt(0).toUpperCase() + name.slice(1)
}

function convertAttrName(name) {
    if (name === 'class') {
        return 'className'
    }

    if (name === 'id') {
        return 'id'
    }

    if (name.indexOf('-') === -1) {
        return name
    }


    const parts = name.split("-")
    return parts.slice(1).reduce((p, c) => p + capitalize(c), parts[0])
}

function convertAttrValue(name, value) {
    if (name === 'style') {
        return convertStyleToObject(value)
    }

    return value
}

function convertStyleToObject(style) {
    const result = {}
    style.split(";").filter(e => e.length)
        .map(splitToPair).forEach(([k, v]) => result[convertAttrName(k)] = v)

    return result

    function splitToPair(kv) {
        const kvSplit = kv.trim().split(':')
        return [kvSplit[0].trim(), kvSplit[1]]
    }
}

export {svgAttributesToProps, convertAttrValue}
