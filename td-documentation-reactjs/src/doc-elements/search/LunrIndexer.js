import lunr from 'lunr'

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
            return lunr.Index.load(JSON.parse(indexJson))
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
        console.log(JSON.stringify(page))
        page.content.filter((de) => de.type === 'Section').
            forEach((s) => this.addSection(page.tocItem, s))
    }

    addSection(pageTocItem, section) {
        const pageSectionTitle = section.title
        const id = {...pageTocItem, pageSectionTitle}

        const text = extractTextFromElement(section)
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

class QueryResult {
    constructor(queryResults) {
        this.snippetsById = {}
        this.queryResults_ = queryResults
        this.flattenResults_()
    }

    getIds() {
        return Object.keys(this.snippetsById)
    }

    getSnippetsToHighlight(id, text) {
        return this.snippetsById[id](text)
    }

    addSnippets_(id, snippetsCallback) {
        this.snippetsById[id] = snippetsCallback
    }

    flattenResults_() {
        this.queryResults_.forEach((queryResult) => {
            const metaData = queryResult.matchData.metadata

            Object.keys(metaData).forEach((word) => {
                const matchByWord = metaData[word]
                Object.keys(matchByWord).forEach((type) => {
                    this.addSnippets_(queryResult.ref, (text) => extractSnippets(text, matchByWord[type].position))
                })
            })
        })
    }
}

function extractTextFromContent(content) {
    if (! content) {
        return ""
    }

    return content.map((el) => extractTextFromElement(el)).join(" ")
}

function extractTextFromElement(docElement) {
    if (docElement.type === 'SimpleText') {
        return docElement.text
    }

    if (docElement.content) {
        return extractTextFromContent(docElement.content)
    }

    return ""
}

function extractSnippets(text, positions) {
    return [... new Set(positions.map((p) => text.substr(p[0], p[1])))]
}

export default LunrIndexer
export {extractTextFromElement}
