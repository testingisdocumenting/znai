const classNamesToSkip = [
    'section',
    'content-block',
    'page-title-block',
    'page-meta-block',
    'page-last-update-time',
    'two-sides-page-content',
    'page-two-sides-layout',
    'page-two-sides-left-part',
    'page-two-sides-right-part',
    'two-sides-section']

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

            const isClassToIgnore = classNamesToSkip.some(cn => classes.indexOf(cn) !== -1)
            if (isClassToIgnore) {
                continue
            }

            if (!bn.isEqualNode(an)) {
                return an
            }
        }
    }
}

export default PageContentPreviewDiff
