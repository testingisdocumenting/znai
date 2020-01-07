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

import {DocumentationTracking} from './DocumentationTracking'

describe('DocumentationTracking', () => {
    it('notifies only listeners with a defined method', () => {
        const tracking = new DocumentationTracking()

        const markerA = []
        const markerB = []

        const listenerA = {
            onPageOpen(pageId) {
                markerA.push('pageOpen')
                markerA.push(pageId)
            }
        }

        const listenerB = {
            onLinkClick(currentPageId, url) {
                markerB.push('onLinkClick')
                markerB.push(currentPageId)
                markerB.push(url)
            }
        }

        tracking.addListener(listenerA)
        tracking.addListener(listenerB)

        tracking.onPageOpen({dirName: 'dir-name'})
        tracking.onLinkClick('http://remote')

        expect(markerA).toEqual(['pageOpen', {dirName: 'dir-name'}])
        expect(markerB).toEqual(['onLinkClick', {dirName: 'dir-name'}, 'http://remote'])
    })
})