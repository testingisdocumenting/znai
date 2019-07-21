/*
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

import * as Prism from 'prismjs'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-groovy'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-javascript'
import 'prismjs/components/prism-python'
import 'prismjs/components/prism-c'
import 'prismjs/components/prism-markdown'
import 'prismjs/components/prism-markup'
import 'prismjs/components/prism-cpp'
import 'prismjs/components/prism-json'
import 'prismjs/components/prism-bash'
import 'prismjs/components/prism-yaml'
import 'prismjs/components/prism-typescript'

export {parseCode}

function parseCode(lang, code) {
    const prismLang = Prism.languages[adjustLang(lang)]

    const tokens = Prism.tokenize(code, prismLang ? prismLang : Prism.languages.clike)
    return tokens.map(t => normalizeToken(t))
}

const extensionsMapping = {
    csv: 'clike',
    html: 'markup',
    yml: 'yaml',
    c: 'cpp',
    h: 'cpp',
    hpp: 'cpp',
    cpp: 'cpp',
    js: 'javascript',
    py: 'python',
    ts: 'typescript',
    tsx: 'typescript',
}

function adjustLang(lang) {
    const extensionBasedLang = extensionsMapping[lang]
    return extensionBasedLang ? extensionBasedLang : lang
}

function normalizeToken(token) {
    if (typeof token === 'string') {
        return token
    }

    if (Array.isArray(token.content)) {
        return {type: token.type, content: token.content.map(t => normalizeToken(t))}
    }

    return {type: token.type, content: token.content}
}
