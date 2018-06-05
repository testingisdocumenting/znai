import lunr from 'lunr'
import searchContent from './searchContent'
import pageContentProcessor from '../pageContentProcessor.js'

/**
 * wraps Lunr result set to flatten it and provide tokens insted of positions for highlighting
 */
class QueryResult {
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

    getSnippetsToHighlight(id, text) {
        const snippets = []

        const snippetsById = this.snippetsCallbacksById[id]
        const snippetsTypeById = this.snippetsTypeById[id]

        const len = snippetsById.length
        for (let i = 0; i < len; i++) {
            const callback = snippetsById[i]
            const type = snippetsTypeById[i]

            if (type === 'text') {
                snippets.push(...callback(text))
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

class LunrIndexer {
    constructor({pages, indexJson}) {
        let that = this
        this.lunr = pages ? createWithPages() : loadFromJson()

        function createWithPages() {
            return lunr(function () {
                this.ref('id')
                this.field('title')
                this.field('snippet')
                this.field('text')
                this.metadataWhitelist = ['position']

                that.idx_ = this
                pages.forEach((p) => that.addPage(p))
                that.outsideOfLunrIndexCreation = true
            })
        }

        function loadFromJson() {
            return lunr.Index.load(indexJson)
        }
    }

    static createWithPages(pages) {
        return new LunrIndexer({pages})
    }

    static createWithJson(indexJson) {
        return new LunrIndexer({indexJson})
    }

    exportAsJson() {
        return JSON.stringify(this.lunr)
    }

    search(term) {
        return new QueryResult(this.lunr.search(term))
    }

    addPage(page) {
        pageContentProcessor.process(page.content).filter((de) => de.type === 'Section')
            .forEach((s) => this.addSection(page.tocItem, s))
    }

    addSection(pageTocItem, section) {
        const pageSectionTitle = section.title
        const id = {dn: pageTocItem.dirName, fn: pageTocItem.fileName, psid: section.id,
            st: pageTocItem.sectionTitle, pt: pageTocItem.pageTitle, pst: pageSectionTitle}

        const text = searchContent.extractTextFromElement(section)
        this.addText(id, pageSectionTitle, text)
    }

    addText(pageIdAsObject, title, text) {
        this.add(pageIdAsObject, title, "text", text)
    }

    addSnippet(id, title, snippet) {
        this.add(id, title, "snippet", snippet)
    }

    add(id, title, type, value) {
        if (this.outsideOfLunrIndexCreation) {
            throw new Error("can't modify index outside of creation phase")
        }

        this.idx_.add({id: JSON.stringify(id), title: title, [type]: value})
    }
}

function extractSnippets(text, positions) {
    return [...new Set(positions.map((p) => text.substr(p[0], p[1])))]
}

export default LunrIndexer
