class PageContentPreviewDiff {
    constructor(before, after) {
        this.before = before
        this.after = after
    }

    findFirstDifferentNode() {
        let btw = document.createTreeWalker(this.before);
        let atw = document.createTreeWalker(this.after);

        for (;;) {
            const bn = btw.nextNode()
            const an = atw.nextNode()

            if (bn === null || an === null) {
                return null
            }

            const classes = (typeof an.className === 'string') ? an.className.split(' ').filter((cn) => cn.length) : []
            if (! classes.length) {
                continue
            }

            // section is not a content-block as it displays sometimes elements that don't fit specified width
            if (classes.indexOf('snippet') === -1 && (
                classes.indexOf("section") !== -1 ||
                classes.indexOf('content-block') !== -1)) {
                continue
            }

            if (!bn.isEqualNode(an)) {
                return an
            }
        }
    }
}

export default PageContentPreviewDiff
