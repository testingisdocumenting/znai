export interface DocMeta {
    previewEnabled: boolean;
    id: string;
}

let docMeta: DocMeta = {previewEnabled: false, id: ''};

function setDocMeta(newDocMeta: DocMeta) {
    docMeta = {...newDocMeta};
}

function isPreviewEnabled() {
    return docMeta.previewEnabled;
}

function getDocId() {
    return docMeta.id;
}

export {setDocMeta, isPreviewEnabled, getDocId};
