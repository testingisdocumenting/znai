import {documentationNavigation} from './structure/DocumentationNavigation'
import {selectedTextExtensions} from './selected-text-extensions/SelectedTextExtensions'

let docMeta = {}

function setDocMeta(newDocMeta) {
    docMeta = newDocMeta
    documentationNavigation.documentationId = docMeta.id // TODO revisit dependency

    registerExtensions()
}

function registerExtensions() {
    if (docMeta.hasOwnProperty('hipchatRoom')) {
        selectedTextExtensions.register({name: 'Ask in HipChat', action: (args) => console.log('hipchat action:', args)})
    }
}

function isPreviewEnabled() {
    return docMeta.previewEnabled
}

function getDocId() {
    return docMeta.id
}

export {setDocMeta, isPreviewEnabled, getDocId}
