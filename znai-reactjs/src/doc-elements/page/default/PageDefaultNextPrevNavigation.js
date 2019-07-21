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

import React from 'react'

import './PageDefaultNextPrevNavigation.css'

function PageDefaultNextPrevNavigation({prevPageTocItem, nextPageTocItem, onNextPage, onPrevPage}) {
    return (
        <div className="page-default-next-prev-navigation">
            <DefaultPrevPageButton tocItem={prevPageTocItem} onClick={onPrevPage}/>
            <DefaultNextPageButton tocItem={nextPageTocItem} onClick={onNextPage}/>
        </div>
    )
}

function DefaultPrevPageButton({tocItem, onClick}) {
    return needRenderNavigationButton(tocItem) ? (
        <div className="default-next-prev-navigation-button" onClick={onClick}>
            <span className="glyphicon glyphicon-chevron-left"/>
            <span className="next-prev-page-title">{tocItem.pageTitle} </span>
        </div>) : <div/>
}

function DefaultNextPageButton({tocItem, onClick}) {
    return needRenderNavigationButton(tocItem) ? (
        <div className="default-next-prev-navigation-button" onClick={onClick}>
            <span className="next-prev-page-title">{tocItem.pageTitle} </span>
            <span className="glyphicon glyphicon-chevron-right"/>
        </div>) : <div/>
}

function needRenderNavigationButton(tocItem) {
    // we don't render next/prev buttons that will point to items without dir name (e.g. index page)
    return tocItem && tocItem.dirName;
}

export {PageDefaultNextPrevNavigation, DefaultPrevPageButton, DefaultNextPageButton}
