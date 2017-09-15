class TextSelection {
    constructor() {
        this.listeners = []
    }

    addListener(listener) {
        this.listeners.push(listener)
    }

    notify({sectionTitle, pageTitle, pageSectionTitle, startNode, text}) {
        this.listeners.forEach(l => l({sectionTitle, pageTitle, pageSectionTitle, startNode, text}))
    }

    notifyClear() {
        this.listeners.forEach(l => l({
            sectionTitle: null,
            pageTitle: null,
            pageSectionTitle: null,
            startNode: null,
            text: null}))
    }
}

const textSelection = new TextSelection()

export {textSelection}
