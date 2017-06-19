let docMeta = {}

function setDocMeta(newDocMeta) {
    docMeta = newDocMeta
}

function isPreviewEnabled() {
    return docMeta.previewEnabled
}

export {setDocMeta, isPreviewEnabled}
