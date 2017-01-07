exports.extractStyleNames = extractStyleNames
exports.removeStyleNames = removeStyleNames

const styleRegexp = /\[(.*?)\]\s*$/

function extractStyleNames(text) {
    return extractTextInBrackets(text).trim().split(' ').filter((i) => i.length)
}

function removeStyleNames(text) {
    return text.replace(styleRegexp, '')
}

function extractTextInBrackets(text) {
    const matches = text.match(styleRegexp)
    return matches ? matches[1] : ""
}

// TODO need a map from ids to styles
// need to have arrow color based on from -> to. Color of a "to"
