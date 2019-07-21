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

function splitTextIntoLines(text, thresholdCharCount) {
    if (text.length < thresholdCharCount) {
        return [text]
    }

    const words = text.split(" ")
    const result = splitParts(words,
        (word) => word.length + 1, // one for space
        thresholdCharCount)

    return result.map(words => words.join(" "))
}

function splitParts(parts, lengthFunc, thresholdCharCount) {
    const result = []
    let runningLength = 0
    let runningWords = []

    parts.forEach(part => {
        let len = lengthFunc(part);

        if ((runningLength + len) >= thresholdCharCount) {
            flush()
        }

        runningLength += len
        runningWords.push(part)
    })

    flush()

    return result

    function flush() {
        if (runningWords.length) {
            result.push(runningWords)
        }

        runningWords = []
        runningLength = 0
    }
}

export {splitTextIntoLines, splitParts}