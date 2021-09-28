/*
 * Copyright 2021 znai maintainers
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

export function removeCustomProps(props) {
    const res = {...props}
    removeCustomPropsNoCopy(res)
    return res
}

export function removeCustomPropsNoCopy(props) {
    delete props.svg
    delete props.url
    delete props.isInvertedTextColor
    delete props.colors
    delete props.parentClassName
    delete props.nodeId
    delete props.diagramId
    delete props.selected
}

export function removeRadiusPropsNoCopy(props) {
    delete props.rx
    delete props.ry
}

export function buildUniqueId(diagramId, nodeId) {
    diagramId = diagramId || "no_name"
    return `${diagramId}_${nodeId}`
}

// if id is for an edge like a->b we want it to be expanded into three ids: a, b, a->b
export function expandId(id) {
    const expanded = [id]
    if (id.indexOf('-') === -1) {
        return expanded
    }

    id.replace('>', '').replace('<', '').replace('--', '-').split('-')
        .forEach((p) => expanded.push(p))

    return expanded
}
