/*
 * Copyright 2020 znai maintainers
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

import {NoBullets} from "./BulletList.jsx";
import RevealBoxes from "./kinds/RevealBoxes.jsx";
import HorizontalStripes from "./kinds/HorizontalStripes.jsx";
import Grid from "./kinds/Grid.jsx";
const presentationTypes = {RevealBoxes, HorizontalStripes, Grid}

export function startsWithIcon(content) {
    return content &&
            content.length && content[0].type === 'Paragraph' &&
            content[0].content.length && content[0].content[0].type === 'Icon'
}

export function extractIconId(content) {
    return extractIconProps(content).id
}

export function extractIconProps(content) {
    return content[0].content[0]
}

export function extractIconIds(list) {
    return list.map(item => startsWithIcon(item.content) ? extractIconId(item.content) : undefined)
}

export function removeIcon(content) {
    const copy = [...content]

    copy[0] = {...copy[0]}
    copy[0].content = copy[0].content.slice(1)

    return copy
}

export function extractTextLines(content) {
    return content.map(item => extractText(item))
}

export function extractTextLinesEmphasisOnly(content) {
    return content.map(item => extractText(item, true))
}

export function extractTextLinesEmphasisOrFull(content) {
    const full = extractTextLines(content)
    const emphasisOnly = extractTextLinesEmphasisOnly(content)
    const result = []

    for (let i = 0, len = full.length; i < len; i++) {
        result.push(emphasisOnly[i] ? emphasisOnly[i] : full[i])
    }

    return result
}

function extractText(listItem, emphasisedOnly) {
    const result = []
    collectTextRecursively(result, listItem.content, emphasisedOnly, false)

    return capitalizeFirstLetter(result.join(" "))
}

function collectTextRecursively(result, content, emphasisedOnly, withinEmphasis) {
    if (! content) {
        return
    }

    content.forEach(item => {
        if (item.type === "SimpleText") {
            if (emphasisedOnly && withinEmphasis) {
                result.push(item.text)
            } else if (! emphasisedOnly) {
                result.push(item.text)
            }
        } else {
            collectTextRecursively(result, item.content, emphasisedOnly, withinEmphasis || isEmphasis(item))
        }
    })

    return result
}

function isEmphasis(docElement) {
    return docElement.type === 'Emphasis' || docElement.type === 'StrongEmphasis'
}

function capitalizeFirstLetter(text) {
    return text.length > 1 ? text.charAt(0).toUpperCase() + text.slice(1) : text;
}


function valueByIdWithWarning(dict, type) {
    if (!Object.hasOwn(dict, type)) {
        console.warn("can't find bullets list type: " + type)
        return NoBullets
    }

    return dict[type]
}

function presentationListType(props) {
    return listType(props, 'bulletListType') ||
        listType(props, 'presentationBulletListType')
}

function listType(props, key) {
    if (! Object.hasOwn(props,'meta')) {
        return null
    }

    const meta = props.meta
    if (! Object.hasOwn(meta, key)) {
        return null
    }

    if (meta[key] === "") {
        return null
    }

    return meta[key]
}


export {listType, presentationListType, presentationTypes, valueByIdWithWarning}