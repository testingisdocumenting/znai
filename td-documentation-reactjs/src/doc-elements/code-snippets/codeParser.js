import Prism from 'prismjs'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-javascript'
import 'prismjs/components/prism-python'
import 'prismjs/components/prism-c'
import 'prismjs/components/prism-markdown'
import 'prismjs/components/prism-cpp'

export {parseCode}

function parseCode(lang, code) {
    if (lang === 'csv') {
        lang = 'clike'
    }

    const tokens = Prism.tokenize(code, Prism.languages[lang])
    return tokens.map(normalizeToken)
}

function normalizeToken(token) {
    if (typeof token === 'string') {
        return {type: "text", data: token}
    }

    return {type: token.type, data: normalizeData(token.content)}
}

function normalizeData(data) {
    if (typeof data === 'string') {
        return data
    }

    if (Array.isArray(data)) {
        return data.map(d => normalizeData(d)).join("")
    }

    if (typeof data === 'object') {
        return data.content
    }

    return JSON.stringify(data)
}