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

    notifyNewTab(name) {
        removeFromArray(this.tabsSelectionHistory, name)
        this.tabsSelectionHistory.unshift(name)

        this.listeners.forEach(l => l(name))
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
