/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
