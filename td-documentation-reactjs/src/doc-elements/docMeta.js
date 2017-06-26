import {documentationNavigation} from './structure/DocumentationNavigation'

let docMeta = {}

function setDocMeta(newDocMeta) {
    docMeta = newDocMeta
    documentationNavigation.documentationId = docMeta.id // TODO revisit dependency
}

function isPreviewEnabled() {
    return docMeta.previewEnabled
}

function getDocId() {
    return docMeta.id
}

export {setDocMeta, isPreviewEnabled, getDocId}
