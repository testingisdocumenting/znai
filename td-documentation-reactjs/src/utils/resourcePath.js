function fullResourcePath(docId, path) {
    return "/" + (docId.length ? docId + "/" : "") + path
}

export {fullResourcePath}
