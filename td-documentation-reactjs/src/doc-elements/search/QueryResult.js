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

function extractSnippets(text, positions) {
    return [...new Set(positions.map((p) => text.substr(p[0], p[1])))]
}