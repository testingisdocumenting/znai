const collectTextRecursively = (content, result) => {
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

const extractText = (listItem) => {
    const result = []
    collectTextRecursively(listItem.content, result)

    return result.join(" ")
}

const extractTextLines = (content) => content.map(item => extractText(item))

export {extractTextLines}
