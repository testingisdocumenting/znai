function startsWithIcon(content) {
    return content.length && content[0].type === 'Paragraph' &&
            content[0].content.length && content[0].content[0].type === 'Icon'
}

function extractIconId(content) {
    return content[0].content[0].id
}

function extractTextLines(content) {
    return content.map(item => extractText(item))
}

function collectTextRecursively(content, result) {
    if (! content) {
        return
    }

    content.forEach(item => {
        if (item.type === "SimpleText") {
            result.push(item.text)
        } else {
            collectTextRecursively(item.content, result)
        }
    })

    return result
}

function extractText(listItem) {
    const result = []
    collectTextRecursively(listItem.content, result)

    return result.join(" ")
}

export {extractTextLines, startsWithIcon, extractIconId}
