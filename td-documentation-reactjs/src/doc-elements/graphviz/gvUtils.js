export {removeCustomProps, buildUniqueId}

function removeCustomProps(props) {
    const res = {...props}
    delete res.svg
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