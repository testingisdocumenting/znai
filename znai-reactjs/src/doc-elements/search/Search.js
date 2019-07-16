import QueryResult from './QueryResult'

class Search {
    constructor(allPages) {
        this.allPages = allPages
        this.searchIdx = window.mdocSearchIdx
        this.searchDataById = mapById(window.mdocSearchData)
    }

    static convertIndexIdToSectionCoords(indexId) {
        const [dirName, fileName, pageSectionId] = indexId.split('@@')
        return {dirName, fileName, pageSectionId}
    }

    search(term) {
        return new QueryResult(this.searchIdx.search(term))
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

        this.allPages.forEach((p) => {
            const tocItem = p.tocItem

            const sections = p.content.filter((de) => {
                return tocItem.dirName === sectionCoords.dirName &&
                    tocItem.fileName === sectionCoords.fileName &&
                    de.type === 'Section' && de.id === sectionCoords.pageSectionId})

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
