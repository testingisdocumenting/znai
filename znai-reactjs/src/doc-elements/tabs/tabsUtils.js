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

import { walkContentNodes } from '../default-elements/contentTreeWalker'

export function contentTabNames(content) {
    const result = []

    walkContentNodes(content, e => {
        if (e.type === 'Tabs') {
            addMissingTabNames(result, e)
            // nested per tab elements live under tabsContent and are handled above, not walked into
            return "skip-children"
        }
    })

    return result
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