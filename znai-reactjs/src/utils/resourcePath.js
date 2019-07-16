function fullResourcePath(docId, path) {
    return "/" + (docId.length ? docId + (path ? "/" : "") : "") + path
}

export {fullResourcePath}
