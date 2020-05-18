/*
 * Copyright 2020 znai maintainers
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

import React from 'react'

import { Icon } from '../../icons/Icon'
import './PageDefaultNextPrevNavigation.css'

function PageDefaultNextPrevNavigation({currentTocItem, prevPageTocItem, nextPageTocItem, onNextPage, onPrevPage}) {
    return (
        <div className="page-default-next-prev-navigation">
            <DefaultPrevPageButton currentTocItem={currentTocItem} prevTocItem={prevPageTocItem} onClick={onPrevPage}/>
            <DefaultNextPageButton currentTocItem={currentTocItem} nextTocItem={nextPageTocItem} onClick={onNextPage}/>
        </div>
    )
}

function DefaultPrevPageButton({currentTocItem, prevTocItem, onClick}) {
    return prevTocItem ? (
        <div className="default-next-prev-navigation-button" onClick={onClick}>
            <Icon id="chevron-left"/>
            <div className="next-prev-page-title prev">{fullTitle(currentTocItem, prevTocItem)} </div>
        </div>) : <div/>
}

function DefaultNextPageButton({currentTocItem, nextTocItem, onClick}) {
    return nextTocItem ? (
        <div className="default-next-prev-navigation-button" onClick={onClick}>
            <div className="next-prev-page-title next">{fullTitle(currentTocItem, nextTocItem)} </div>
            <Icon id="chevron-right"/>
        </div>) : <div/>
}

function fullTitle(current, nextOrPrev) {
    if (current.dirName === nextOrPrev.dirName) {
        return nextOrPrev.pageTitle;
    }

    return `${nextOrPrev.sectionTitle}: ${nextOrPrev.pageTitle}`
}

export {PageDefaultNextPrevNavigation, DefaultPrevPageButton, DefaultNextPageButton}
