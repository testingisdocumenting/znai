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

export default class QueryResult {
    constructor(queryResults) {
        this.snippetsCallbacksById = {}
        this.snippetsTypeById = {}
        this._queryResults = queryResults
        this._flattenResults()
    }

    // ids are string versions of json for performance and to be compatible with lunr
    getIds() {
        return Object.keys(this.snippetsCallbacksById)
    }

    getSnippetsToHighlight(id, searchEntry) {
        const snippets = []

        const snippetsById = this.snippetsCallbacksById[id]
        const snippetsTypeById = this.snippetsTypeById[id]

        const len = snippetsById.length
        for (let idx = 0; idx < len; idx++) {
            const callback = snippetsById[idx]
            const type = snippetsTypeById[idx]

            console.log("@@type", type, "searchEntry", searchEntry)
            if (type === 'textStandard') {
                snippets.push(...callback(searchEntry.textStandard))
            } else if (type === 'textHigh') {
                snippets.push(...callback(searchEntry.textHigh))
            }
        }

        return snippets
    }

    _addSnippets(id, type, snippetsCallback) {
        if (this.snippetsCallbacksById.hasOwnProperty(id)) {
            this.snippetsCallbacksById[id].push(snippetsCallback)
            this.snippetsTypeById[id].push(type)
        } else {
            this.snippetsCallbacksById[id] = [snippetsCallback]
            this.snippetsTypeById[id] = [type]
        }
    }

    _flattenResults() {
        this._queryResults.forEach((queryResult) => {
            const metaData = queryResult.matchData.metadata

            Object.keys(metaData).forEach((word) => {
                const matchByWord = metaData[word];

                Object.keys(matchByWord).forEach((type) => {
                    this._addSnippets(queryResult.ref, type, (text) => extractSnippets(text, matchByWord[type].position))
                })
            })
        })
    }
}

function extractSnippets(text, positions) {
    return [...new Set(positions.map((p) => text.substr(p[0], p[1])))]
}