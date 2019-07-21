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

export function schemaToApiParameters(schema) {
    const converted = convertEntry('root', schema)
    return Array.isArray(converted) ? converted : [converted]
}

function convertEntry(name, entry) {
    if (isObject(entry)) {
        return convertObject(entry.properties)
    }

    if (isArrayOfObject(entry)) {
        return convertArrayOfObject(name, entry)
    }

    return convertSimple(name, entry)
}

function convertSimple(name, entry) {
    let result = {name, type: fullType(entry)}
    if (entry.description) {
        result.description = entry.description
    }

    return result
}

function convertObject(object) {
    const result = []
    Object.keys(object).forEach(k => {
        const v = object[k]
        if (isObject(v)) {
            result.push({...convertSimple(k, v), children: convertObject(v.properties)})
        } else if (isArrayOfObject(v)) {
            result.push(convertArrayOfObject(k, v))
        } else {
            result.push(convertSimple(k, v))
        }
    })

    return result
}

function convertArrayOfObject(name, entry) {
    return {...convertSimple(name, entry), children: convertObject(entry.items.properties)}
}

function fullType(entry) {
    if (isObject(entry)) {
        return 'object'
    }

    if (isArrayOfSimple(entry)) {
        return 'array of ' + typeAndFormat(entry.items)
    }

    if (isArrayOfObject(entry)) {
        return 'array of objects'
    }

    return typeAndFormat(entry)
}

function typeAndFormat(entry) {
    return entry.type + (entry.format ? '(' + entry.format + ')' : '')
}

function isObject(item) {
    return item.hasOwnProperty('properties')
}

function isArray(item) {
    return item.type === 'array'
}

function isArrayOfSimple(item) {
    return isArray(item) && ! isObject(item.items)
}

function isArrayOfObject(item) {
    return isArray(item) && isObject(item.items)
}
