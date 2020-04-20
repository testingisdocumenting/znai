/*
 * Copyright 2020 znai maintainers
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

import * as React from 'react'

import {PageDefaultNextPrevNavigation} from './PageDefaultNextPrevNavigation'
import {simpleAction} from 'react-component-viewer'

const actions = {
    onNextPage: simpleAction('next page'),
    onPrevPage: simpleAction('prev page')
}

export function defaultNextPrevNavigationDemo(registry) {
    registry
        .add('current chapter is the same as the next', () => (
            <PageDefaultNextPrevNavigation currentTocItem={tocItem('Chapter A', 'Page Two')}
                                           prevPageTocItem={tocItem('Chapter A', 'Page One')}
                                           nextPageTocItem={tocItem('Chapter A', 'Page Three')}
                                           {...actions}/>))
        .add('previous chapter is different than current', () => (
            <PageDefaultNextPrevNavigation prevPageTocItem={tocItem('Chapter A', 'Page Two')}
                                           currentTocItem={tocItem('Chapter B', 'Page One')}
                                           nextPageTocItem={tocItem('Chapter B', 'Page Two')}
                                           {...actions}/>))
        .add('next chapter is different than current', () => (
            <PageDefaultNextPrevNavigation prevPageTocItem={tocItem('Chapter A', 'Page One')}
                                           currentTocItem={tocItem('Chapter A', 'Page Two')}
                                           nextPageTocItem={tocItem('Chapter B', 'Page One')}
                                           {...actions}/>))
}

function tocItem(sectionTitle, pageTitle) {
    return {
        dirName: sectionTitle.toLowerCase(),
        fileName: pageTitle.toLowerCase(),
        sectionTitle,
        pageTitle
    }
}