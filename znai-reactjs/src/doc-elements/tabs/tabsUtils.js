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

export function contentTabNames(content) {
    const result = []

    collectTabNamesRecursively(result, content)

    return result
}

function collectTabNamesRecursively(result, content) {
    if (!content || content.length === 0) {
        return
    }

    content.forEach(e => {
        if (e.type === 'Tabs') {
            addMissingTabNames(result, e)
        } else {
            collectTabNamesRecursively(result, e.content)
        }
    })
}

function addMissingTabNames(result, tabsDocEl) {
    tabsDocEl.tabsContent.forEach(tc => addMissing(result, tc.name))
}

function addMissing(result, name) {
    const idx = result.indexOf(name)
    if (idx !== -1) {
        return
    }

    result.push(name)
}