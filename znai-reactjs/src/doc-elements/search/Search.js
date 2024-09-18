/*
 * Copyright 2021 znai maintainers
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

import QueryResult from './QueryResult'

class Search {
    constructor(allPages) {
        this.allPages = allPages
        this.searchIdx = window.znaiSearchIdx
        this.searchDataById = mapById(window.znaiSearchData)
    }

    static convertIndexIdToSectionCoords(indexId) {
        const [dirName, fileName, pageSectionId] = indexId.split('@@')
        return {dirName, fileName, pageSectionId}
    }

    search(term) {
        const lunr = window.lunr
        const highestBoost = 2
        const highBoost = 1.5
        const defaultBoost = 0.5
        const lowestBoost = 0.05
        const matches = this.searchIdx.query(q => {
            term.split(lunr.tokenizer.separator).forEach(function (term) {
                q.term(term, { fields: [ 'pageTitle' ], boost: highestBoost })
                q.term(term, { fields: [ 'pageSection' ], boost: highestBoost })
                q.term(term, { fields: [ 'textStandard' ], boost: defaultBoost })
                q.term(term, { fields: [ 'textHigh' ], boost: highBoost })

                // add wildcard search for long enough queries that don't have * in them
                if (term.length >= 3 && term.indexOf('*') === -1) {
                    q.term(term, { fields: ['pageTitle'], boost: highestBoost, wildcard: lunr.Query.wildcard.TRAILING })
                    q.term(term, { fields: ['pageSection'], boost: highestBoost, wildcard: lunr.Query.wildcard.TRAILING })
                    q.term(term, { fields: ['textStandard'], boost: lowestBoost, wildcard: lunr.Query.wildcard.TRAILING })
                    q.term(term, { fields: ['textHigh'], boost: highBoost, wildcard: lunr.Query.wildcard.TRAILING })
                }
            })
        })

        return new QueryResult(matches)
    }

    findSearchEntryById(id) {
        return this.searchDataById[id]
    }

    previewDetails(id, queryResult) {
        const section = this._findSectionById(id)
        const snippets = queryResult.getSnippetsToHighlight(id, this.findSearchEntryById(id).text)

        return {section, snippets}
    }

    _findSectionById(indexId) {
        const sectionCoords = Search.convertIndexIdToSectionCoords(indexId)

        const matching = []

        this.allPages.pages.forEach((p) => {
            const tocItem = p.tocItem

            const sections = p.content.filter((de) => {
                return tocItem.dirName === sectionCoords.dirName &&
                    tocItem.fileName === sectionCoords.fileName &&
                    de.type === 'Section' && (!sectionCoords.pageSectionId || de.id === sectionCoords.pageSectionId)})

            sections.forEach((s) => matching.push(s))
        })

        if (! matching) {
            console.error("expected section associated with", indexId)
        }

        return matching[0]
    }
}

function mapById(searchData) {
    const result = {}
    searchData.forEach(([id, section, pageTitle, pageSection, text]) => {
        result[id] = {section, pageTitle, pageSection, text}
    })

    return result
}

export default Search
