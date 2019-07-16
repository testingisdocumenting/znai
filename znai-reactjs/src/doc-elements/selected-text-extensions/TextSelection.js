import {getDocId} from '../docMeta'

class TextSelection {
    constructor() {
        this.listeners = []
    }

    addListener(listener) {
        this.listeners.push(listener)
    }

    startSelection({pageSectionTitle, pageSectionId}) {
        this.selectionSectionInfo = {pageSectionTitle, pageSectionId}
    }

    endSelection({tocItem, startNode, text}) {
        const url = "/" + getDocId() + "/" + tocItem.dirName + "/" + tocItem.fileName +
            (this.selectedPageSectionId ? "#" + this.selectedPageSectionId : "")

        this.listeners.forEach(l => l({...this.selectionSectionInfo,
            sectionTitle: tocItem.sectionTitle,
            pageTitle: tocItem.pageTitle,
            startNode, text,
            url}))

        this.selectionSectionInfo = {}
    }

    get selectedPageSectionId() {
        return this.selectionSectionInfo.pageSectionId
    }

    clear() {
        this.selectionSectionInfo = {}
        this.listeners.forEach(l => l({
            sectionTitle: null,
            pageTitle: null,
            pageSectionTitle: null,
            startNode: null,
            text: null}))
    }
}

const textSelection = new TextSelection()

export {textSelection}
