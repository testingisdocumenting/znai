import React from 'react'

import './PageTitle.css'

const PageTitle = ({tocItem, onPresentationOpen, lastModifiedTime, docMeta, elementsLibrary}) => {
    const displayTitle = tocItem.dirName.length && tocItem.fileName !== "index"
    const title = displayTitle ? [<span key="title" className="page-title">{tocItem.pageTitle}</span>,
        onPresentationOpen ?
            <span key="button" className="presentation-button glyphicon glyphicon-resize-full"
                  onClick={onPresentationOpen}/> : null] : []

    return (
        <React.Fragment>
            <div className="page-title-block">
                {title}
            </div>
            <div className="page-meta-block">
                <ModifiedTime lastModifiedTime={lastModifiedTime}/>
                <ViewOn docMeta={docMeta} tocItem={tocItem}/>
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
        <div className="page-last-update-time">
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
            <a href={buildViewOnLink(tocItem, viewOn.link)} target="_bank">{viewOn.title}</a>
        </div>
    )
}

function buildViewOnLink(tocItem, link) {
    return `${link}/${tocItem.dirName}/${tocItem.fileName}.md`
}


export default PageTitle
