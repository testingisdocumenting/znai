import React from 'react'

import './PageDefaultNextPrevNavigation.css'

function PageDefaultNextPrevNavigation({prevPageTocItem, nextPageTocItem, onNextPage, onPrevPage}) {
    return (
        <div className="page-default-next-prev-navigation content-block">
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
