import React, {Component} from 'react'
import lunr from 'lunr'
import Mark from 'mark.js/dist/mark.js'

import elementsLibrary from '../DefaultElementsLibrary'

class LunrIndexer {
    constructor(callbackToAddDocs) {
        let that = this
        this.lunr = lunr(function () {
            this.ref('id')
            this.field('title')
            this.field('snippet')
            this.field('text')
            this.metadataWhitelist = ['position']

            that.idx_ = this
            callbackToAddDocs(that)
            that.outsideOfLunrIndexCreation = true
        })
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

const extractedText = extractTextFromContent(sampleSectionContent().content)


const indexer = new LunrIndexer((idx) => {
    idx.addText({title: "sec1/name1", id: "id1"}, "section name", extractedText)
    idx.addText({title: "sec2/name2", id: "id2"}, "section1 name2", "fghj broth")
})

const indexAsJson = JSON.stringify(indexer.lunr)

let loaded = lunr.Index.load(JSON.parse(indexAsJson))
const queryResult = loaded.search("common")

const matchData = queryResult
const snippetsByType = extractSnippetsByType(extractedText, matchData)
const displayResult = JSON.stringify(queryResult) + "\n" +
        extractedText + "\n---\n" +
        JSON.stringify(matchData) + "\n +++ \n" +
        JSON.stringify(snippetsByType)

function extractSnippetsByType(text, queryResults) {
    let result = []
    queryResults.forEach((queryResult) => {
        const metaData = queryResult.matchData.metadata
        Object.keys(metaData).forEach((word) => {
            const matchByWord = metaData[word]
            Object.keys(matchByWord).forEach((type) => {
                result.push({type: type, snippets: extractSnippets(text, matchByWord[type].position)})
            })
        })
    })

    return result
}

function extractSnippets(text, positions) {
    return [...new Set(positions.map((p) => text.substr(p[0], p[1])))]
}

class SearchResultPreview extends Component {
    componentDidMount() {
        console.log(this.dom)
        const mark = new Mark(this.dom);

        snippetsByType.forEach((st) => {
            mark.mark(st.snippets, {accuracy: "exactly"})

        })
    }

    render() {
        return (<div className="search-result-preview" ref={(dom) => this.dom = dom}>{this.props.children}</div>)
    }
}

// const result = JSON.stringify(indexer.lunr.search('sec/name'))
const LunrIndexerDebug = () => <div>
    <pre>{displayResult}</pre>
    <SearchResultPreview><elementsLibrary.DocElement content={sampleSectionContent().content}/></SearchResultPreview>
</div>
{/*const LunrIndexerDebug = () => <div><pre>{result}</pre></div>*/
}

export default LunrIndexerDebug

function sampleSectionContent() {
    return {
        "type": "Section",
        "content": [{
            "type": "Paragraph",
            "content": [
                {
                    "text": "Before you bring external software into Two Sigma, you must verify is that the external software has a license that is acceptable to Two Sigma. The following licenses are known to be acceptable:",
                    "type": "SimpleText"
                }
            ]
        },
            {
                "bulletMarker": "-",
                "tight": true,
                "type": "BulletList",
                "content": [
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "BSD",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "Apache",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "MIT",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "LGPL",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "Tcl/Tk",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "CDDL",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "EPL",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "type": "ListItem",
                        "content": [
                            {
                                "type": "Paragraph",
                                "content": [
                                    {
                                        "text": "Creative Commons",
                                        "type": "SimpleText"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Do not bring in GPL licensed libraries",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Libraries licensed using the GPL, which is distinct from LGPL, have been deemed unacceptable.",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "If your external code uses a license that is not listed here, you should contact Christos and the Legal team.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "GPL-licensed applications intended to be directly run from a shell",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "(as opposed to libraries that will be invoked from our code)",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "can be added as external codebases to our tree under ",
                        "type": "SimpleText"
                    },
                    {
                        "text": " or ",
                        "type": "SimpleText"
                    },
                    {
                        "text": ",",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "depending on the version of the license they are using.",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "Guarding these applications (or the GPL-licensed files therein) from being referenced is recommended.",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "It can be done by setting ",
                        "type": "SimpleText"
                    },
                    {
                        "text": " to an empty string or ",
                        "type": "SimpleText"
                    },
                    {
                        "text": " in ",
                        "type": "SimpleText"
                    },
                    {
                        "type": "SoftLineBreak"
                    },
                    {
                        "text": "for the imported module.",
                        "type": "SimpleText"
                    }
                ]
            }
        ]
    }

}

