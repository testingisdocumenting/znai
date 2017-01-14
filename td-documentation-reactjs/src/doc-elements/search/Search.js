import LunrIndexer from './LunrIndexer'
import searchContent from './searchContent'

class Search {
    constructor(allPages, searchIndex) {
        this.allPages = allPages
        this.searchIndex = searchIndex
        this.lunrIndexer = LunrIndexer.createWithJson(searchIndex)
    }

    search(term) {
        return this.lunrIndexer.search(term)
    }

    previewDetails(id, queryResult) {
        const section = this.findSectionById_(id)
        const snippets = queryResult.getSnippetsToHighlight(id, searchContent.extractTextFromElement(section))

        return {section, snippets}
    }

    findSectionById_(indexId) {
        indexId = JSON.parse(indexId)

        const matching = []

        this.allPages.forEach((p) => {
            const tocItem = p.tocItem

            const sections = p.content.filter((de) => {
                return tocItem.dirName === indexId.dirName &&
                    tocItem.fileName === indexId.fileName &&
                    de.type === 'Section' && de.title === indexId.pageSectionTitle})

            sections.forEach((s) => matching.push(s))
        })

        if (! matching) {
            console.error("expected section associated with", indexId)
        }

        return matching[0]
    }
}

export default Search
