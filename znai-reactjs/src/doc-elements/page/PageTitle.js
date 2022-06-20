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

import { Support } from "./Support"
import { Icon } from '../icons/Icon'

import "./PageTitle.css"

const PageTitle = ({tocItem, onPresentationOpen, lastModifiedTime, docMeta}) => {
    const displayTitle = tocItem.dirName.length && tocItem.fileName !== "index"
    const title = displayTitle ? [<span key="title" className="page-title">{tocItem.pageTitle}</span>,
        onPresentationOpen ?
            <Icon key="presentation-mode"
                  id="maximize"
                  className="presentation-button"
                  onClick={onPresentationOpen}/> : null] : []

    return (
        <React.Fragment>
            <div className="page-title-block">
                {title}
            </div>
            <div className="page-meta-block">
                <ModifiedTime lastModifiedTime={lastModifiedTime}/>
                <ViewOn docMeta={docMeta} tocItem={tocItem}/>
                <Support/>
            </div>
        </React.Fragment>
    )
}

function ModifiedTime({lastModifiedTime}) {
    if (!lastModifiedTime) {
        return null
    }

    const modifiedTimeAsStr = new Date(lastModifiedTime).toDateString()
    return (
        <div className="znai-page-last-update-time">
            {modifiedTimeAsStr}
        </div>
    )
}

function ViewOn({docMeta, tocItem}) {
    const viewOn = docMeta.viewOn
    if (!viewOn || !viewOn.link || !viewOn.title) {
        return null
    }

    return (
        <div className="page-view-on">
            <a href={buildViewOnLink(tocItem, viewOn.link)}
               target="_blank" rel="noopener">
                {viewOn.title}
            </a>
        </div>
    )
}

function buildViewOnLink(tocItem, link) {
    return tocItem.viewOnRelativePath ?
        `${link}/${tocItem.viewOnRelativePath}` :
        `${link}/${tocItem.dirName}/${tocItem.fileName}.md`
}

export default PageTitle
