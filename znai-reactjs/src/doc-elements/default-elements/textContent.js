export function extractTextFromContent(content) {
    if (! content) {
        return ""
    }

    return content.map((el) => extractTextFromElement(el)).join(" ")
}

export function extractTextFromElement(docElement) {
    if (docElement.type === 'SimpleText') {
        return docElement.text
    }

    if (docElement.content) {
        return extractTextFromContent(docElement.content)
    }

    return ""
}
