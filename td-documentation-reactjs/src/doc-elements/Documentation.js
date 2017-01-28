import React, { Component } from "react"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import elementsLibrary from './DefaultElementsLibrary'
import {documentationNavigation} from './DocumentationNavigation'
import {getAllPagesPromise} from "./allPages"

import Preview from './Preview'
import PreviewChangeIndicator from './PreviewChangeIndicator'
import PageContentPreviewDiff from './PageContentPreviewDiff'

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
        this.onMultiplePagesUpdate = this.onMultiplePagesUpdate.bind(this)

        documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
    }

    render() {
        const {toc, docMeta} = this.props
        const {page, tocCollapsed, tocSelected} = this.state

        const pageTitle = page.tocItem.pageTitle

        const searchPopup = this.state.searchActive ? <SearchPopup searchPromise={this.searchPromise}
                                                                   onSearchSelection={this.onSearchSelection}
                                                                   onClose={this.onSearchClose}/> : null

        const preview = docMeta.previewEnabled ? <Preview active={true}
                                                          onPageUpdate={this.onPageUpdate}
                                                          onMultiplePagesUpdate={this.onMultiplePagesUpdate}/> : null

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

    onNextPage() {
        const {page} = this.state
        if (page.nextTocItem) {
            documentationNavigation.navigateToPage(page.nextTocItem)
        }
    }

    onPrevPage() {
        const {page} = this.state
        if (page.prevTocItem) {
            documentationNavigation.navigateToPage(page.prevTocItem)
        }
    }

    onSearchSelection(id) {
        this.onSearchClose()
        documentationNavigation.navigateToPage(id)
    }

    navigateToPageIfRequired(tocItem) {
        const currentToc = this.state.page.tocItem

        if (currentToc.dirName !== tocItem.dirName || currentToc.fileName !== tocItem.fileName) {
            documentationNavigation.navigateToPage(tocItem)
        }
    }

    // one markup page was changed and view needs to be updated
    //
    onPageUpdate(pageProps) {
        const updatePagesReference = () => getAllPagesPromise().then((allPages) => {
            this.updatePagesReference(allPages, pageProps);
        })

        this.navigateToPageIfRequired(pageProps.tocItem)

        this.updatePageAndDetectChangePosition(() => {
            updatePagesReference()
            this.setState({page: pageProps})
        })

        // if (currentToc.dirName !== pageProps.tocItem.dirName || currentToc.fileName !== pageProps.tocItem.fileName) {
        //     documentationNavigation.navigateToPage(pageProps.tocItem)
        // } else {
        //     this.updatePageAndDetectChangePosition(() => this.setState({page: pageProps}))
        // }
    }

    // one of the files referred from a markup or multiple markups was changed
    // we need to update multiple pages at once and either refresh current view (if one of the changed pages is it)
    // or navigate to the first modified page
    //
    onMultiplePagesUpdate(listOfPageProps) {
        getAllPagesPromise().then((allPages) => {
            listOfPageProps.forEach((newPage) => this.updatePagesReference(allPages, newPage))
        })

        const currentToc = this.state.page.tocItem
        const matchingPages = listOfPageProps.filter((newPage) => currentToc.dirName === newPage.tocItem.dirName &&
            currentToc.fileName === newPage.tocItem.fileName)

        if (matchingPages.length) {
            documentationNavigation.navigateToPage(matchingPages[0].tocItem)
        } else {
            documentationNavigation.navigateToPage(listOfPageProps[0].tocItem)
        }
    }

    updatePageAndDetectChangePosition(funcToUpdatePage) {
        const nodeBefore = document.querySelector(".page-content")
        const htmlBefore = nodeBefore.innerHTML.slice(0)

        funcToUpdatePage()

        const nodeAfter = document.querySelector(".page-content")
        const htmlAfter = nodeAfter.innerHTML.slice(0)

        const previewDiff = new PageContentPreviewDiff(htmlBefore, htmlAfter)
        let closestDataId = previewDiff.findClosestDataId();

        if (closestDataId !== -1) {
            const closestDom = document.querySelector(`[data-id='${closestDataId}']`)
            this.setState({lastChangeDataDom: closestDom})
        } else {
            this.setState({lastChangeDataDom: null})
            console.error("can't find closest data id")
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
        getAllPagesPromise().then((pages) => {
            const currentPageLocation = documentationNavigation.extractDirNameAndFileName(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === currentPageLocation.dirName &&
                p.tocItem.fileName === currentPageLocation.fileName)

            if (! matchingPages.length) {
                console.error("can't find any page with", currentPageLocation)
                return
            }

            this.setState({page: matchingPages[0], selectedTocItem: currentPageLocation, lastChangeDataDom: null})
        })
    }
}

export default Documentation
