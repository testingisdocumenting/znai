function paragraphStartsWith(content, text) {
    if (! content.length) {
        return false
    }

    if (content[0].type !== 'SimpleText') {
        return false
    }

    return content[0].text.startsWith(text)
}

function removeSuffixFromParagraph(content, suffix) {
    if (! paragraphStartsWith(content, suffix)) {
        return content
    }

    let copy = content.slice(0)
    copy[0] = {...content[0]}

    const currentText = copy[0].text
    copy[0].text = currentText.substr(suffix.length + (currentText[suffix.length] === ' ' ? 1 : 0))

    if (! copy[0].text.length) {
        copy = copy.slice(1)
    }

    return copy
}

export {paragraphStartsWith, removeSuffixFromParagraph}