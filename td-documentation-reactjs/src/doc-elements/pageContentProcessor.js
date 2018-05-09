function process(pageContent) {
    return createSectionWithEmptyTitle(
        mergeMetaIntoContent(pageContent))
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

    let merged = {...element}

    if (newContent) {
        merged.content = newContent
    }

    if (meta) {
        merged.meta = meta
    }

    return merged
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

export default {process}
