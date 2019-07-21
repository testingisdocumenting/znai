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

class TabsRegistration {
    constructor() {
        this.listeners = []
        this.tabsSelectionHistory = []
    }

    addTabSwitchListener(listener) {
        this.listeners.push(listener)
    }

    removeTabSwitchListener(listener) {
        removeFromArray(this.listeners, listener)
    }

    firstMatchFromHistory(names) {
        const matches = this.tabsSelectionHistory.filter(n => names.indexOf(n) >= 0)
        return matches ? matches[0] : names[0]
    }

    notifyNewTab({tabName, triggeredNode}) {
        removeFromArray(this.tabsSelectionHistory, tabName)
        this.tabsSelectionHistory.unshift(tabName)

        this.listeners.forEach(l => l({tabName, triggeredNode}))
    }
}

function removeFromArray(array, value) {
    const idx = array.indexOf(value)
    if (idx !== -1) {
        array.splice(idx, 1)
    }
}

const tabsRegistration = new TabsRegistration()

export {tabsRegistration}
