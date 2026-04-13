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

function process(pageContent) {
    return annotateFootnoteOccurrences(
        createSectionWithEmptyTitle(
            mergeMetaIntoContent(pageContent)))
}

// meta doc elements props will be merged
// into a next doc element and all its children
function mergeMetaIntoContent(pageContent, meta) {
    let currentMeta = meta

    const result = []
    if (!pageContent) {
        return result
    }

    for (let i = 0, len = pageContent.length; i < len; i++) {
        const el = pageContent[i]
        if (el.type === 'Meta') {
            // eslint-disable-next-line no-unused-vars
            const {type, ...meta} = el
            currentMeta = {...currentMeta, ...meta}
        } else {
            result.push(mergeMetaIntoElement(el, currentMeta))
        }
    }

    return result
}

function mergeMetaIntoElement(element, meta) {
    const newContent = element.content ? mergeMetaIntoContent(element.content, meta) : element.content

    const merged = {...element}

    if (newContent) {
        merged.content = newContent
    }

    if (meta) {
        merged.meta = {...meta, ...merged.meta}
    }

    return merged
}

function annotateFootnoteOccurrences(content) {
    const occurrences = {}
    return annotateFootnoteOccurrencesRec(content, occurrences)
}

function annotateFootnoteOccurrencesRec(content, occurrences) {
    if (!content) {
        return content
    }

    return content.map(el => {
        const newContent = el.content ? annotateFootnoteOccurrencesRec(el.content, occurrences) : el.content

        if (el.type === 'FootnoteReference') {
            occurrences[el.label] = (occurrences[el.label] || 0) + 1
            return {...el, content: newContent, occurrence: occurrences[el.label]}
        }

        return newContent !== el.content ? {...el, content: newContent} : el
    })
}

function extractFootnotes(content) {
    const byLabel = new Map()
    collectFootnoteReferences(content, byLabel)
    return [...byLabel.values()]
}

function collectFootnoteReferences(content, byLabel) {
    if (!content) {
        return
    }

    for (const el of content) {
        if (el.type === 'FootnoteReference') {
            const existing = byLabel.get(el.label)
            if (existing) {
                existing.refCount++
            } else {
                byLabel.set(el.label, {label: el.label, content: el.content, refCount: 1})
            }
        }

        collectFootnoteReferences(el.content, byLabel)
    }
}

/**
 * section is the first class citizen. smallest unit of search and navigation.
 * if a user didn't specify a section, the default section wrapper will be created.
 *
 */
function createSectionWithEmptyTitle(pageContent) {
    const firstSectionIdx = findFirstSectionIdx(pageContent)
    let isSectionAbsent = firstSectionIdx === -1;

    const elementsOutsideSection = isSectionAbsent ?
        pageContent :
        pageContent.slice(0, firstSectionIdx)

    const restOfElements = isSectionAbsent ?
        [] :
        pageContent.slice(firstSectionIdx)

    return elementsOutsideSection.length ?
        [{type: "Section", title: "", id: "", content: elementsOutsideSection}].concat(restOfElements):
        pageContent
}

function findFirstSectionIdx(content) {
    for (let i = 0, len = content.length; i < len; i++) {
        if (content[i].type === 'Section') {
            return i
        }
    }

    return -1
}

export const pageContentProcessor = {process, extractFootnotes}
