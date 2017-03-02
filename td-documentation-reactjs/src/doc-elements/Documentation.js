import React, { Component } from "react"
import Promise from "promise"

import TocPanel from './structure/TocPanel'
import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import elementsLibrary from './DefaultElementsLibrary'
import DocumentationNavigation from './structure/DocumentationNavigation'
import {tableOfContents} from './structure/TableOfContents'
import {getAllPagesPromise} from "./allPages"
import {fullResourcePath} from '../utils/resourcePath'

import Preview from './preview/Preview'
import PreviewChangeIndicator from './preview/PreviewChangeIndicator'
import PageContentPreviewDiff from './preview/PageContentPreviewDiff'

import './DocumentationLayout.css'
import './search/Search.css'

class Documentation extends Component {
    constructor(props) {
        super(props)

        const {page, docMeta} = this.props
        this.searchPromise = getSearchPromise(docMeta)

        this.documentationNavigation = new DocumentationNavigation(docMeta.id)

        const currentPageLocation = this.documentationNavigation.currentDirNameAndFileName()

        const selectedTocItem = {...currentPageLocation, pageSectionId: page.tocItem.pageSectionIdTitles[0].id}
        this.state = {
            tocCollapsed: false,
            tocSelected: false,
            page: page,
            toc: tableOfContents.toc,
            selectedTocItem: selectedTocItem}

        this.onHeaderClick = this.onHeaderClick.bind(this)
        this.onTocToggle = this.onTocToggle.bind(this)
        this.onTocSelect = this.onTocSelect.bind(this)
        this.onTocItemClick = this.onTocItemClick.bind(this)
        this.onSearchClick = this.onSearchClick.bind(this)
        this.onSearchClose = this.onSearchClose.bind(this)
        this.onPanelSelect = this.onPanelSelect.bind(this)
        this.onNextPage = this.onNextPage.bind(this)
        this.onPrevPage = this.onPrevPage.bind(this)
        this.onSearchSelection = this.onSearchSelection.bind(this)
        this.onPageUpdate = this.onPageUpdate.bind(this)
        this.onTocUpdate = this.onTocUpdate.bind(this)
        this.onMultiplePagesUpdate = this.onMultiplePagesUpdate.bind(this)
        this.onPageGenError = this.onPageGenError.bind(this)
        this.updateCurrentPageSection = this.updateCurrentPageSection.bind(this)

        this.documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
    }

    render() {
        const {docMeta} = this.props
        const {toc, page, selectedTocItem, tocCollapsed, tocSelected, pageGenError} = this.state

        const searchPopup = this.state.searchActive ? <SearchPopup searchPromise={this.searchPromise}
                                                                   onSearchSelection={this.onSearchSelection}
                                                                   onClose={this.onSearchClose}/> : null

        const preview = docMeta.previewEnabled ? <Preview active={true}
                                                          onPageUpdate={this.onPageUpdate}
                                                          onMultiplePagesUpdate={this.onMultiplePagesUpdate}
                                                          onTocUpdate={this.onTocUpdate}
                                                          onError={this.onPageGenError}/> : null

        const previewIndicator = <PreviewChangeIndicator targetDom={this.state.lastChangeDataDom}/>

        const pageGenErrorPanel = pageGenError ? (<div className="page-gen-error">{pageGenError}</div>) : null

        return (
            <div className="documentation">
                <div className="side-panel" onClick={this.onTocSelect}>
                    <TocPanel toc={toc} collapsed={tocCollapsed} selected={tocSelected}
                              docMeta={docMeta}
                              onToggle={this.onTocToggle}
                              selectedItem={selectedTocItem}
                              onHeaderClick={this.onHeaderClick}
                              onTocItemClick={this.onTocItemClick}
                              onNextPage={this.onNextPage}
                              onPrevPage={this.onPrevPage}/>
                </div>

                {preview}
                {previewIndicator}

                <div className="search-button glyphicon glyphicon-search" onClick={this.onSearchClick}/>

                {searchPopup}

                <div className="main-panel" onClick={this.onPanelSelect} ref={panelDom => this.mainPanelDom = panelDom}>
                    <elementsLibrary.Page tocItem={page.tocItem}
                                          content={page.content}
                                          previewEnabled={docMeta.previewEnabled}/>
                    <div className="next-prev-buttons content-block">
                        {this.renderPreviousPageButton()}
                        {this.renderNextPageButton()}
                    </div>
                </div>

                {pageGenErrorPanel}
            </div>)
    }

    componentDidMount() {
        this.enableScrollListener()
        this.onPageLoad()
    }

    componentWillUnmount() {
        this.disableScrollListener()
    }

    enableScrollListener() {
        // server side rendering guard
        if (window.addEventListener) {
            this.mainPanelDom.addEventListener('scroll', this.updateCurrentPageSection)
        }
    }

    disableScrollListener() {
        // server side rendering guard
        if (window.removeEventListener) {
            this.mainPanelDom.removeEventListener('scroll', this.updateCurrentPageSection)
        }
    }

    changePage(newStateWithNewPage) {
        this.setState({...newStateWithNewPage, pageGenError: null})
        this.onPageLoad()
    }

    scrollToTop() {
        this.mainPanelDom.scrollTop = 0
    }

    onPageLoad() {
        this.extractPageSectionNodes()
        this.updateCurrentPageSection()
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
            this.documentationNavigation.navigateToPage(next)
        }
    }

    onPrevPage() {
        const prev = this.prevPageToc
        if (prev) {
            this.documentationNavigation.navigateToPage(prev)
        }
    }

    onHeaderClick() {
        const url = fullResourcePath(this.props.docMeta.id, "")
        this.documentationNavigation.navigateToUrl(url)
    }

    onTocItemClick(dirName, fileName) {
        this.documentationNavigation.navigateToPage({dirName, fileName})
    }

    static doRenderNavigationButton(tocItem) {
        // we don't render next/prev buttons that will point to items without dir name (e.g. index page)
        return tocItem && tocItem.dirName;
    }

    renderNextPageButton() {
        return (Documentation.doRenderNavigationButton(this.nextPageToc) ? (
                <div className="page-navigation-button-and-text" onClick={this.onNextPage}>
                        <span className="next-prev-page-title">{this.nextPageToc.pageTitle} </span>
                        <span className="glyphicon glyphicon-chevron-right"/>
                </div>) : <div/>)
    }

    renderPreviousPageButton() {
        return (Documentation.doRenderNavigationButton(this.prevPageToc) ? (
                <div className="page-navigation-button-and-text" onClick={this.onPrevPage}>
                    <span className="glyphicon glyphicon-chevron-left"/>
                    <span className="next-prev-page-title">{this.prevPageToc.pageTitle} </span>
                </div>) : <div/>)
    }

    onSearchSelection(id) {
        this.onSearchClose()
        this.documentationNavigation.navigateToPage({dirName: id.dn, fileName: id.fn, pageSectionId: id.psid})
    }

    navigateToPageIfRequired(tocItem) {
        const currentToc = this.state.page.tocItem

        if (currentToc.dirName !== tocItem.dirName || currentToc.fileName !== tocItem.fileName) {
            return this.documentationNavigation.navigateToPage(tocItem)
        }

        return Promise.resolve(true)
    }

    onTocUpdate(toc) {
        tableOfContents.toc = toc
        this.setState({toc})
        if (! tableOfContents.hasTocItem(this.state.page.tocItem)) {
            this.documentationNavigation.navigateToPage(tableOfContents.first)
        }
    }

    // one markup page was changed and view needs to be updated
    //
    onPageUpdate(pageProps) {
        const updatePagesReference = () => this.getAllPagesPromise().then((allPages) => {
            this.updatePagesReference(allPages, pageProps);
        })

        this.navigateToPageAndDisplayChange(pageProps, updatePagesReference)
    }

    // one of the files referred from a markup or multiple markups was changed
    // we need to update multiple pages at once and either refresh current view (if one of the changed pages is it)
    // or navigate to the first modified page
    //
    onMultiplePagesUpdate(listOfPageProps) {
        const updatePagesReference = () => this.getAllPagesPromise().then((allPages) => {
            listOfPageProps.forEach((newPage) => this.updatePagesReference(allPages, newPage))
        })

        const currentToc = this.state.page.tocItem
        const matchingPages = listOfPageProps.filter((newPage) => currentToc.dirName === newPage.tocItem.dirName &&
            currentToc.fileName === newPage.tocItem.fileName)

        if (matchingPages.length) {
            this.updatePageAndDetectChangePosition(() => {
                updatePagesReference()
                this.changePage({page: matchingPages[0]})
            })
        } else {
            this.navigateToPageAndDisplayChange(listOfPageProps[0], updatePagesReference)
        }
    }

    onPageGenError(error) {
        console.error(error)
        this.setState({pageGenError: error})
    }

    navigateToPageAndDisplayChange(pageProps, updatePagesReference) {
        this.navigateToPageIfRequired(pageProps.tocItem).then(() => {
            this.updatePageAndDetectChangePosition(() => {
                updatePagesReference()
                this.changePage({page: pageProps})
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
            this.disableScrollListener();
            this.setState({lastChangeDataDom: differentNode}, this.enableScrollListener)
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
        return this.getAllPagesPromise().then((pages) => {
            const currentPageLocation = this.documentationNavigation.extractDirNameAndFileName(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === currentPageLocation.dirName &&
                p.tocItem.fileName === currentPageLocation.fileName)

            if (! matchingPages.length) {
                console.error("can't find any page with", currentPageLocation)
                return
            }

            const tocItem = matchingPages[0].tocItem
            this.changePage({page: matchingPages[0], selectedTocItem: currentPageLocation, lastChangeDataDom: null})
            this.scrollToTop()

            const sectionIdx = tocItem.pageSectionIdTitles.map(ps => ps.id).indexOf(currentPageLocation.pageSectionId);
            if (sectionIdx >= 0) {
                this.pageSectionNodes[sectionIdx].scrollIntoView();
            }

            return true
        })
    }

    getAllPagesPromise() {
        const {docMeta} = this.props
        return getAllPagesPromise(docMeta)
    }

    extractPageSectionNodes() {
        this.pageSectionNodes = [...document.querySelectorAll(".section-title")]
    }

    updateCurrentPageSection() {
        const pageSections = this.state.page.tocItem.pageSectionIdTitles
        let sectionTitles = this.pageSectionNodes.map((n, idx) => { return {idTitle: pageSections[idx], rect: n.getBoundingClientRect()}})
        const height = window.innerHeight

        const withVisibleTitle = sectionTitles.filter(st => {
            const rect = st.rect
            return rect.top > -10 && rect.top < height
        })

        const closestToTopZero = () => {
            let belowZero = sectionTitles.filter(st => st.rect.top < 0)
            return belowZero.length ? belowZero[belowZero.length - 1] : sectionTitles[0]
        }

        const current = withVisibleTitle.length ? withVisibleTitle[0] : closestToTopZero()

        const selectedTocItem = {...this.state.selectedTocItem, pageSectionId: current.idTitle.id}
        this.setState({selectedTocItem})
    }
}

export default Documentation
