/*
 * Copyright 2024 znai maintainers
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

var createStopWordFilter = function (stopWords) {
    var words = stopWords.reduce(function (memo, stopWord) {
        memo[stopWord] = stopWord
        return memo
    }, {})
    return function (token) {
        if (token && words[token.toString()] !== token.toString()) return token
    }
}

var stopWordFilter = createStopWordFilter([
    'a',
    'am',
    'an',
    'at',
    'be',
    'so',
    'to'
])

znaiSearchIdx = lunr(function () {
    this.pipeline.remove(lunr.stemmer)
    this.ref('id')
    this.field('section')
    this.field('pageTitle')
    this.field('pageSection')
    this.field('textStandard')
    this.field('textHigh')

    this.metadataWhitelist = ['position']

    znaiSearchData.forEach(function (e) {
        this.add({
            id: e[0],
            section: e[1],
            pageTitle: e[2],
            pageSection: e[3],
            textStandard: e[4],
            textHigh: e[5],
        })
    }, this)
})
