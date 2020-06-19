/*
 * Copyright 2020 znai maintainers
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

import {
    countNumberOfLines,
    repeatChar,
    splitAndTrimEmptyLines,
    splitParts,
    splitTextIntoLinesUsingThreshold
} from './strings'

describe("string utils", () => {
    it("should split text into lines and remove empty lines from both sides", () => {
        expect(splitAndTrimEmptyLines('')).toEqual([])
        expect(splitAndTrimEmptyLines('hello')).toEqual(['hello'])
        expect(splitAndTrimEmptyLines('hello\nline')).toEqual(['hello', 'line'])
        expect(splitAndTrimEmptyLines('hello\nline\n')).toEqual(['hello', 'line'])
        expect(splitAndTrimEmptyLines('\nhello\nline')).toEqual(['hello', 'line'])
        expect(splitAndTrimEmptyLines('   \nline')).toEqual(['line'])
        expect(splitAndTrimEmptyLines('line\n     ')).toEqual(['line'])
        expect(splitAndTrimEmptyLines('     line\n     ')).toEqual(['     line'])
        expect(splitAndTrimEmptyLines('     \nline\n     ')).toEqual(['line'])
    })

    it("should split text into one line if threshold is not reached", () => {
        const lines = splitTextIntoLinesUsingThreshold("hello", 10)
        expect(lines).toEqual(["hello"])
    })

    it("should split text into two lines using spaces as dilimiter", () => {
        const lines = splitTextIntoLinesUsingThreshold("hello world to", 13)
        expect(lines).toEqual(["hello world", "to"])
    })

    it("should split text as soon as threshold is reached", () => {
        const lines = splitTextIntoLinesUsingThreshold("hello world to", 1)
        expect(lines).toEqual(["hello", "world", "to"])
    })

    it("should move the last long token to a new line", () => {
        const lines = splitTextIntoLinesUsingThreshold("hello world-with-no-spaces", 12)
        expect(lines).toEqual(["hello", "world-with-no-spaces"])
    })

    it("should split into parts using threshold", () => {
        const parts = splitParts({
            parts: ["hello", "world", "of", "parts", "and", "configs"],
            lengthFunc: (p) => p.length,
            thresholdCharCount: 11
        })

        expect(parts).toEqual([['hello', 'world'], ['of', 'parts', 'and'], ['configs']])
    })

    it("should split into parts after specified split parts", () => {
        const parts = splitParts({
            parts: ["hello", "world", "of", "parts", "and", "configs"],
            lengthFunc: (p) => p.length,
            valueFunc: (p) => p,
            thresholdCharCount: 20,
            splitAfterList: ['world', 'and']
        })

        expect(parts).toEqual([['hello', 'world'], ['of', 'parts', 'and'], ['configs']])
    })

    it("should split respecting threshold when split parts are specified", () => {
        const parts = splitParts({
            parts: ["hello", "world", "of", "par", "and", "ter", "configs"],
            lengthFunc: (p) => p.length,
            valueFunc: (p) => p,
            thresholdCharCount: 11,
            splitAfterList: ['and']
        })

        expect(parts).toEqual([['hello', 'world'], ['of', 'par', 'and'], ['ter', 'configs']])
    })

    it("should repeat char N times", () => {
        expect(repeatChar(0, ' ')).toEqual('')
        expect(repeatChar(3, ' ')).toEqual('   ')
    })

    it("counts number of lines", () => {
        expect(countNumberOfLines("")).toEqual(1);
        expect(countNumberOfLines("hello")).toEqual(1);
        expect(countNumberOfLines("hello\nworld")).toEqual(2);
        expect(countNumberOfLines("hello\nworld\n")).toEqual(2);
        expect(countNumberOfLines("hello\nworld\nof")).toEqual(3);
    })
})