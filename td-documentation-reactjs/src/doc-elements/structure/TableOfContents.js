/**

Represents a table of contents for the documentation.

unlike allPages and searchIndex we load toc as part of page load and not as a separate fetch call.
It is not as big in size and is essential for page rendering. Also enables server side rendering in a better way.

 */
class TableOfContents {
    constructor(toc) {
        this.toc = toc
    }

    set toc(toc) {
        this.tocItems = []
        toc.forEach(s => s.items.forEach(ti => this.tocItems.push(ti)))
        this._toc = toc;
    }

    get toc() {
        return [...this._toc];
    }

    get first() {
        return this.tocItems[0]
    }

    hasTocItem(tocItem) {
        return this.tocItems.filter((ti) => ti.dirName === tocItem.dirName && ti.fileName === tocItem.fileName).length > 0
    }

    nextTocItem(tocItem) {
        for (let i = 0, len = this.tocItems.length; i < len; i++) {
            const ti = this.tocItems[i]
            if (ti.fileName === tocItem.fileName && ti.dirName === tocItem.dirName) {
                return (i + 1) < len ? this.tocItems[i + 1] : null
            }
        }

        return null
    }

    prevTocItem(tocItem) {
        for (let i = this.tocItems.length - 1; i >= 0; i--) {
            const ti = this.tocItems[i]
            if (ti.fileName === tocItem.fileName && ti.dirName === tocItem.dirName) {
                return (i - 1) >= 0 ? this.tocItems[i - 1] : null
            }
        }

        return null
    }
}

const tableOfContents = new TableOfContents(global.toc || [])

export {tableOfContents, setTocJson}

/**
 * need a way to update TOC from preview mode
 * @param tocJson json string representing a TOC
 */
function setTocJson(tocJson) {
    tableOfContents.toc = JSON.parse(tocJson)
}

