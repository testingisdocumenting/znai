class PageContentPreviewDiff {
    constructor(before, after) {
        this.before = before.replace(/ __recently-modified/g, '')
        this.after = after.replace(/ __recently-modified/g, '')
    }

    /**
     * we need to find the closest identification of an element near change.
     * React stopped using data-reactid on a pure client side but still generates them during
     * server side rendering. using them for now
     */
    findClosestDataId() {
        const indexOfDiff = this.findFirstDiffIndex();

        const commonPrefix = this.before.substr(0, indexOfDiff + 1)
        const idxOfId = commonPrefix.lastIndexOf("data-id")

        const reactIdString = commonPrefix.substr(idxOfId)
        return reactIdString.split("\n")[0].replace(/.*?=["'](\d+?)["'].*/, "$1")
    }

    findFirstDiffIndex() {
        const lenBefore = this.before.length
        const lenAfter = this.after.length
        const minLen = Math.min(lenBefore, lenAfter)
        for (let i = 0; i < minLen; i++) {
            if (this.before[i] !== this.after[i]) {
                return i
            }
        }

        return -1
    }
}

export default PageContentPreviewDiff
