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
        let btw = document.createTreeWalker(this.before)
        let atw = document.createTreeWalker(this.after)

        for (;;) {
            const bn = btw.nextNode()
            const an = atw.nextNode()

            if (bn === null && an !== null) {
                return an
            }

            if (bn === null || an === null) {
                return null
            }

            if (ignoreNode(an)) {
                continue
            }

            if (!bn.isEqualNode(an)) {
                return an
            }
        }
    }
}

function ignoreNode(node) {
    const classes = (typeof node.className === 'string') ? node.className.split(' ').filter((cn) => cn.length) : []
    if (! classes.length) {
        return true
    }

    return classNamesToSkip.some(cn => classes.indexOf(cn) !== -1)

}

export default PageContentPreviewDiff
