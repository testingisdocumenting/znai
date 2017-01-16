class DocumentationNavigation {
    constructor() {
        this.listeners = []

        // server side rendering guard
        if (window.addEventListener) {
            window.addEventListener('popstate', (e) => {
                this.notifyNewUrl(document.location.pathname)
            })
        }
    }

    addUrlChangeListener(listener) {
        this.listeners.push(listener)
    }

    navigateToPage(dirName, fileName) {
        // TODO handle .. for cases when selected is not a doc page
        const url = "../" + dirName + "/" + fileName

        history.pushState({}, null, url)
        this.notifyNewUrl(url)
    }

    notifyNewUrl(url) {
        console.log("notifyNewUrl", url)
        this.listeners.forEach((l) => {
            l(url)
        })
    }

    currentDirNameAndFileName() {
        return window.location ?
            this.extractDirNameAndFileName(window.location.pathname):
            "/server/side"
    }

    extractDirNameAndFileName(url) {
        const parts = url.split("/")
        if (parts.length < 2) {
            return {}
        }

        // something/dir-name/file-name
        // ../dir-name/file-name
        return {dirName: parts[parts.length - 2], fileName: parts[parts.length - 1]}
    }
}

const documentationNavigation = new DocumentationNavigation()

console.log(documentationNavigation.extractDirNameAndFileName("../getting-started/introduction"))
console.log(documentationNavigation.extractDirNameAndFileName("/getting-started/introduction"))

export {documentationNavigation}