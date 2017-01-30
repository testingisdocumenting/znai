import React, { Component } from "react"
import Promise from "promise"

import NavBar from './NavBar'
import TocPanel from './structure/TocPanel'
import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import elementsLibrary from './DefaultElementsLibrary'
import {documentationNavigation} from './structure/DocumentationNavigation'
import {tableOfContents} from './structure/TableOfContents'
import {getAllPagesPromise} from "./allPages"

import Preview from './preview/Preview'
import PreviewChangeIndicator from './preview/PreviewChangeIndicator'
import PageContentPreviewDiff from './preview/PageContentPreviewDiff'

import './DocumentationLayout.css'
import './search/Search.css'

class Documentation extends Component {
    constructor(props) {
        super(props)

        this.searchPromise = getSearchPromise()

        const currentPageLocation = documentationNavigation.currentDirNameAndFileName()
        this.state = {
            tocCollapsed: false,
            tocSelected: false,
            page: this.props.page,
            toc: tableOfContents.toc,
            selectedTocItem: currentPageLocation}

        this.onTocToggle = this.onTocToggle.bind(this)
        this.onSearchClick = this.onSearchClick.bind(this)
        this.onSearchClose = this.onSearchClose.bind(this)
        this.onTocSelect = this.onTocSelect.bind(this)
        this.onPanelSelect = this.onPanelSelect.bind(this)
        this.onNextPage = this.onNextPage.bind(this)
        this.onPrevPage = this.onPrevPage.bind(this)
        this.onSearchSelection = this.onSearchSelection.bind(this)
        this.onPageUpdate = this.onPageUpdate.bind(this)
        this.onTocUpdate = this.onTocUpdate.bind(this)
        this.onMultiplePagesUpdate = this.onMultiplePagesUpdate.bind(this)

        documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
    }

    render() {
        const {docMeta} = this.props
        const {toc, page, tocCollapsed, tocSelected} = this.state

        const pageTitle = page.tocItem.pageTitle

        const searchPopup = this.state.searchActive ? <SearchPopup searchPromise={this.searchPromise}
                                                                   onSearchSelection={this.onSearchSelection}
                                                                   onClose={this.onSearchClose}/> : null

        const preview = docMeta.previewEnabled ? <Preview active={true}
                                                          onPageUpdate={this.onPageUpdate}
                                                          onMultiplePagesUpdate={this.onMultiplePagesUpdate}
                                                          onTocUpdate={this.onTocUpdate}/> : null

        const previewIndicator = <PreviewChangeIndicator targetDom={this.state.lastChangeDataDom}/>

        return (
            <div className="documentation">
                <div className="side-panel" onClick={this.onTocSelect}>
                    <TocPanel toc={toc} collapsed={tocCollapsed} selected={tocSelected}
                              onToggle={this.onTocToggle}
                              selectedItem={this.state.selectedTocItem}
                              onNextPage={this.onNextPage}
                              onPrevPage={this.onPrevPage}/>
                </div>

                {preview}
                {previewIndicator}

                <div className="search-button glyphicon glyphicon-search" onClick={this.onSearchClick}/>

                {searchPopup}

                <div className="main-panel" onClick={this.onPanelSelect}>
                    <NavBar docMeta={docMeta} pageTitle={pageTitle}/>
                    <elementsLibrary.Page title={pageTitle} content={page.content}/>
                    <div className="next-prev-buttons content-block">
                        {this.previousPageButton()}
                        {this.nextPageButton()}
                    </div>
                </div>
            </div>)
    }

    onSearchClick() {
        this.setState({searchActive: true, tocSelected: false})
    }

    onSearchClose() {
        this.setState({searchActive: false})
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }

    onTocSelect() {
        this.setState({tocSelected: true})
    }

    onPanelSelect() {
        this.setState({tocSelected: false})
    }

    get nextPageToc() {
        const {page} = this.state
        return tableOfContents.nextTocItem(page.tocItem)
    }

    get prevPageToc() {
        const {page} = this.state
        return tableOfContents.prevTocItem(page.tocItem)
    }

    onNextPage() {
        const next = this.nextPageToc
        if (next) {
            documentationNavigation.navigateToPage(next)
        }
    }

    onPrevPage() {
        const prev = this.prevPageToc
        if (prev) {
            documentationNavigation.navigateToPage(prev)
        }
    }

    navButtonPage(tocItem, className, handler) {
        return (tocItem ? (<div className={className} onClick={handler}>
            <span className="next-prev-section-title">{tocItem.sectionTitle} </span>
            <span className="next-prev-page-title">{tocItem.pageTitle}</span>
        </div>) : null)
    }

    nextPageButton() {
        return this.navButtonPage(this.nextPageToc, "next-button", this.onNextPage)
    }

    previousPageButton() {
        return this.navButtonPage(this.prevPageToc, "prev-button", this.onPrevPage)
    }

    onSearchSelection(id) {
        this.onSearchClose()
        documentationNavigation.navigateToPage(id)
    }

    navigateToPageIfRequired(tocItem) {
        const currentToc = this.state.page.tocItem

        if (currentToc.dirName !== tocItem.dirName || currentToc.fileName !== tocItem.fileName) {
            return documentationNavigation.navigateToPage(tocItem)
        }

        return Promise.resolve(true)
    }

    onTocUpdate(toc) {
        tableOfContents.toc = toc
        this.setState({toc})
        if (! tableOfContents.hasTocItem(this.state.page.tocItem)) {
            documentationNavigation.navigateToPage(tableOfContents.first)
        }
    }

    // one markup page was changed and view needs to be updated
    //
    onPageUpdate(pageProps) {
        const updatePagesReference = () => getAllPagesPromise().then((allPages) => {
            this.updatePagesReference(allPages, pageProps);
        })

        this.navigateToPageAndDisplayChange(pageProps, updatePagesReference)
    }

    // one of the files referred from a markup or multiple markups was changed
    // we need to update multiple pages at once and either refresh current view (if one of the changed pages is it)
    // or navigate to the first modified page
    //
    onMultiplePagesUpdate(listOfPageProps) {
        const updatePagesReference = () => getAllPagesPromise().then((allPages) => {
            listOfPageProps.forEach((newPage) => this.updatePagesReference(allPages, newPage))
        })

        const currentToc = this.state.page.tocItem
        const matchingPages = listOfPageProps.filter((newPage) => currentToc.dirName === newPage.tocItem.dirName &&
            currentToc.fileName === newPage.tocItem.fileName)

        if (matchingPages.length) {
            this.updatePageAndDetectChangePosition(() => {
                updatePagesReference()
                this.setState({page: matchingPages[0]})
            })
        } else {
            this.navigateToPageAndDisplayChange(listOfPageProps[0], updatePagesReference)
        }
    }

    navigateToPageAndDisplayChange(pageProps, updatePagesReference) {
        this.navigateToPageIfRequired(pageProps.tocItem).then(() => {
            this.updatePageAndDetectChangePosition(() => {
                updatePagesReference()
                this.setState({page: pageProps})
            })
        }).then(() => {}, (error) => console.error(error))
    }

    updatePageAndDetectChangePosition(funcToUpdatePage) {
        const nodeBefore = document.querySelector(".page-content").cloneNode(true)

        funcToUpdatePage()

        const nodeAfter = document.querySelector(".page-content")

        const previewDiff = new PageContentPreviewDiff(nodeBefore, nodeAfter)
        const differentNode = previewDiff.findFirstDifferentNode()

        if (differentNode) {
            this.setState({lastChangeDataDom: differentNode})
        }
    }

    updatePagesReference(allPages, newPage) {
        allPages.filter((page) =>
            page.tocItem.fileName === newPage.tocItem.fileName && page.tocItem.dirName === newPage.tocItem.dirName
        ).forEach((page) => {
            page.content = newPage.content
        })
    }

    onUrlChange(url) {
        return getAllPagesPromise().then((pages) => {
            const currentPageLocation = documentationNavigation.extractDirNameAndFileName(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === currentPageLocation.dirName &&
                p.tocItem.fileName === currentPageLocation.fileName)

            if (! matchingPages.length) {
                console.error("can't find any page with", currentPageLocation)
                return
            }

            this.setState({page: matchingPages[0], selectedTocItem: currentPageLocation, lastChangeDataDom: null})
            return true
        })
    }
}

export default Documentation
