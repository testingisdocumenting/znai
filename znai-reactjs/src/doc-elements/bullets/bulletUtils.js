function startsWithIcon(content) {
    return content &&
            content.length && content[0].type === 'Paragraph' &&
            content[0].content.length && content[0].content[0].type === 'Icon'
}

function extractIconId(content) {
    return content[0].content[0].id
}

function removeIcon(content) {
    const copy = [...content]

    copy[0] = {...copy[0]}
    copy[0].content = copy[0].content.slice(1)

    return copy
}

function extractTextLines(content) {
    return content.map(item => extractText(item))
}

function extractTextLinesEmphasisOnly(content) {
    return content.map(item => extractText(item, true))
}

function extractTextLinesEmphasisOrFull(content) {
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

export {extractTextLines, extractTextLinesEmphasisOnly, extractTextLinesEmphasisOrFull, startsWithIcon, extractIconId, removeIcon}
