import Promise from "promise"

class DocumentationNavigation {
    constructor(documentationId) {
        this.documentationId = documentationId
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
        const url =  "/" + (this.documentationId  ? this.documentationId + "/" : "") + (id.dirName + "/" + id.fileName) +
            (id.pageSectionId ? ("#" + id.pageSectionId) : "")

        this.navigateToUrl(url)
    }

    navigateToUrl(url) {
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

        const parts = url.split("/").filter(p => p !== ".." && p.length > 0)

        if (parts.length < 2) {
            return {dirName: "", fileName: "index"}
        }

        // something/dir-name/file-name#id
        // /dir-name/file-name
        return {dirName: parts[parts.length - 2], fileName: parts[parts.length - 1], pageSectionId: pageSectionId}
    }
}

export default DocumentationNavigation