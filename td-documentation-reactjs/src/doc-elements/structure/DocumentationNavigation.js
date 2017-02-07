import Promise from "promise"

class DocumentationNavigation {
    constructor() {
        this.listeners = []

        // server side rendering guard
        if (window.addEventListener) {
            window.addEventListener('popstate', (e) => {
                this.notifyNewUrl(document.location.pathname + (document.location.hash))
            })
        }
    }

    addUrlChangeListener(listener) {
        this.listeners.push(listener)
    }

    navigateToPage(id) {
        // TODO handle .. for cases when selected is not a doc page
        const url = ("../" + id.dirName + "/" + id.fileName) + (id.pageSectionId && id.pageSectionId.length ? ("#" + id.pageSectionId) : "")

        history.pushState({}, null, url)
        return this.notifyNewUrl(url)
    }

    notifyNewUrl(url) {
        const promises = this.listeners.map((l) => l(url))
        return Promise.all(promises)
    }

    currentDirNameAndFileName() {
        return window.location ?
            this.extractDirNameAndFileName(window.location.pathname):
            "/server/side"
    }

    extractDirNameAndFileName(url) {
        const hashIdx = url.indexOf("#");
        const pageSectionId = hashIdx >= 0 ? url.substr(hashIdx + 1) : ""
        url = hashIdx >= 0 ? url.substr(0, hashIdx) : url

        const parts = url.split("/")
        if (parts.length < 2) {
            return {}
        }

        // something/dir-name/file-name#id
        // ../dir-name/file-name
        return {dirName: parts[parts.length - 2], fileName: parts[parts.length - 1], pageSectionId: pageSectionId}
    }
}

const documentationNavigation = new DocumentationNavigation()

export {documentationNavigation}