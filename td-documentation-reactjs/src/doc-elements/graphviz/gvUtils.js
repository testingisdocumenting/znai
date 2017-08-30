export {removeCustomProps, buildUniqueId, expandId}

function removeCustomProps(props) {
    const res = {...props}
    delete res.svg
    delete res.url
    delete res.isInvertedTextColor
    delete res.colors
    delete res.parentClassName
    delete res.nodeId
    delete res.diagramId
    delete res.selected

    return res
}

function buildUniqueId(diagramId, nodeId) {
    diagramId = diagramId || "no_name"
    return `${diagramId}_${nodeId}`
}

// if id is for an edge like a->b we want it to be expanded into three ids: a, b, a->b
function expandId(id) {
    const expanded = [id]
    if (id.indexOf('-') === -1) {
        return expanded
    }

    id.replace('>', '').replace('<', '').replace('--', '-').split('-')
        .forEach((p) => expanded.push(p))

    return expanded
}
