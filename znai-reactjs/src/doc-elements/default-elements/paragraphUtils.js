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

function paragraphStartsWith(content, text) {
    if (! content.length) {
        return false
    }

    if (content[0].type !== 'SimpleText') {
        return false
    }

    return trimLeft(content[0].text).startsWith(text)
}

function removeSuffixFromParagraph(content, suffix) {
    if (! paragraphStartsWith(content, suffix)) {
        return content
    }

    let copy = content.slice(0)
    copy[0] = {...content[0]}

    const currentText = trimLeft(copy[0].text)
    copy[0].text = currentText.substr(suffix.length + (currentText[suffix.length] === ' ' ? 1 : 0))

    if (! copy[0].text.length) {
        copy = copy.slice(1)
    }

    return copy
}

function trimLeft(text) {
    let nonSpaceIdx = 0;
    for (let i = 0, len = text.length; i < len; i++) {
        if (text[i] === ' ') {
            nonSpaceIdx++
        } else {
            break;
        }
    }

    return text.substr(nonSpaceIdx)
}

export {paragraphStartsWith, removeSuffixFromParagraph}