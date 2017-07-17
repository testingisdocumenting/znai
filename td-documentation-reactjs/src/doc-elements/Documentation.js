import React, { Component } from 'react'
import Promise from 'promise'

import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import {elementsLibrary, presentationElementHandlers} from './DefaultElementsLibrary'
import {documentationNavigation} from './structure/DocumentationNavigation'
import {tableOfContents} from './structure/TableOfContents'
import {getAllPagesPromise} from './allPages'
import {fullResourcePath} from '../utils/resourcePath'

import Presentation from './presentation/Presentation'
import Preview from './preview/Preview'
import PreviewChangeIndicator from './preview/PreviewChangeIndicator'
import PageContentPreviewDiff from './preview/PageContentPreviewDiff'

import PresentationRegistry from './presentation/PresentationRegistry'

import {setDocMeta} from './docMeta'
import DocumentationLayout from './DocumentationLayout'

import pageContentProcessor from './pageContentProcessor.js'

import './DocumentationLayout.css'
import './search/Search.css'

class Documentation extends Component {
    constructor(props) {
        super(props)

        const {page, docMeta} = this.props
        setDocMeta(docMeta)
        this.searchPromise = getSearchPromise(docMeta)

        const currentPageLocation = documentationNavigation.currentPageLocation()

        const selectedTocItem = {...currentPageLocation, pageSectionId: page.tocItem.pageSectionIdTitles[0] ?
            page.tocItem.pageSectionIdTitles[0].id : null}

        this.state = {
            tocCollapsed: false,
            tocSelected: false,
            page: Documentation.processPage(page),
            toc: tableOfContents.toc,
            selectedTocItem: selectedTocItem}

        this.onHeaderClick = this.onHeaderClick.bind(this)
        this.onPresentationOpen = this.onPresentationOpen.bind(this)
        this.onPresentationClose = this.onPresentationClose.bind(this)
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
        this.keyDownHandler = this.keyDownHandler.bind(this)

        documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
    }

    render() {
        const {docMeta} = this.props
        const {
            toc,
            page,
            selectedTocItem,
            tocCollapsed,
            lastChangeDataDom,
            isSearchActive,
            pageGenError,
            isPresentationMode,
            presentationRegistry} = this.state

        const searchPopup = isSearchActive ? <SearchPopup elementsLibrary={elementsLibrary}
                                                                   tocCollapsed={tocCollapsed}
                                                                   searchPromise={this.searchPromise}
                                                                   onSearchSelection={this.onSearchSelection}
                                                                   onClose={this.onSearchClose}/> : null

        const renderedPage = <elementsLibrary.Page tocItem={page.tocItem}
                                                   content={page.content}
                                                   onPresentationOpen={this.onPresentationOpen}
                                                   previewEnabled={docMeta.previewEnabled}
                                                   elementsLibrary={elementsLibrary}/>

        const preview = docMeta.previewEnabled ? <Preview active={true}
                                                          onPageUpdate={this.onPageUpdate}
                                                          onMultiplePagesUpdate={this.onMultiplePagesUpdate}
                                                          onTocUpdate={this.onTocUpdate}
                                                          onError={this.onPageGenError}/> : null

        return isPresentationMode ? <Presentation docMeta={docMeta}
                                              presentationRegistry={presentationRegistry}
                                              onClose={this.onPresentationClose}
                                              onNextPage={this.onNextPage}
                                              onPrevPage={this.onPrevPage}/> :
            <span>
                <DocumentationLayout docMeta={docMeta}
                                     toc={toc}
                                     selectedTocItem={selectedTocItem}
                                     prevPageToc={this.prevPageToc}
                                     nextPageToc={this.nextPageToc}
                                     searchPopup={searchPopup}
                                     renderedPage={renderedPage}
                                     onHeaderClick={this.onHeaderClick}
                                     onSearchClick={this.onSearchClick}
                                     onTocItemClick={this.onTocItemClick}
                                     onNextPage={this.onNextPage}
                                     onPrevPage={this.onPrevPage}
                                     pageGenError={pageGenError}/>
                {preview}
                <PreviewChangeIndicator targetDom={lastChangeDataDom}/>
            </span>
    }

    componentDidMount() {
        this.enableScrollListener()
        this.onPageLoad()
        document.addEventListener("keydown", this.keyDownHandler)
    }

    componentWillUnmount() {
        this.disableScrollListener()
        document.removeEventListener("keydown", this.keyDownHandler)
    }

    keyDownHandler(e) {
        const {isSearchActive, isPresentationMode} = this.state
        if (e.code === "Slash" && ! isSearchActive) {
            e.preventDefault()
            this.setState({isSearchActive: true})
        } else if (e.code === "KeyP" && ! isPresentationMode) {
            this.setState({isPresentationMode: true})
        }
    }

    enableScrollListener() {
        // server side rendering guard
        if (window.addEventListener) {
            this.mainPanelDom = document.querySelector(".main-panel")
            this.mainPanelDom.addEventListener("scroll", this.updateCurrentPageSection)
        }
    }

    disableScrollListener() {
        // server side rendering guard
        if (window.removeEventListener) {
            this.mainPanelDom.removeEventListener("scroll", this.updateCurrentPageSection)
        }
    }

    changePage(newStateWithNewPage) {
        this.setState({...newStateWithNewPage,
            page: Documentation.processPage(newStateWithNewPage.page),
            pageGenError: null})

        this.onPageLoad()
    }

    static processPage(page) {
        return {...page, content: pageContentProcessor.process(page.content)}
    }

    scrollToTop() {
        this.mainPanelDom.scrollTop = 0
    }

    onPageLoad() {
        const {docMeta} = this.props
        const {page} = this.state

        this.extractPageSectionNodes()
        this.scrollToPageSection(page.tocItem, documentationNavigation.currentPageLocation().pageSectionId)
        this.updateCurrentPageSection()
        const presentationRegistry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, page)

        const isIndex = page.tocItem.dirName.length === 0 && page.tocItem.fileName === "index"
        document.title = isIndex ? docMeta.title : docMeta.title + ": " + page.tocItem.pageTitle

        this.setState({presentationRegistry})
    }

    onSearchClick() {
        this.setState({isSearchActive: true})
    }

    onSearchClose() {
        this.setState({isSearchActive: false})
    }

    onTocToggle(collapsed) {
        this.setState({tocCollapsed: collapsed})
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

    onHeaderClick() {
        const url = fullResourcePath(this.props.docMeta.id, "")
        documentationNavigation.navigateToUrl(url)
    }

    onPresentationOpen() {
        this.setState({isPresentationMode: true})
    }

    onPresentationClose() {
        this.setState({isPresentationMode: false})
    }

    onTocItemClick(dirName, fileName) {
        documentationNavigation.navigateToPage({dirName, fileName})
    }

    onSearchSelection(id) {
        this.onSearchClose()
        documentationNavigation.navigateToPage({dirName: id.dn, fileName: id.fn, pageSectionId: id.psid})
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
        const foundPages = allPages.filter((page) =>
            page.tocItem.fileName === newPage.tocItem.fileName && page.tocItem.dirName === newPage.tocItem.dirName
        )

        if (foundPages.length) {
            foundPages.forEach((page) => { page.content = newPage.content })
        } else {
            allPages.push(newPage)
        }
    }

    onUrlChange(url) {
        return this.getAllPagesPromise().then((pages) => {
            const currentPageLocation = documentationNavigation.extractPageLocation(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === currentPageLocation.dirName &&
                p.tocItem.fileName === currentPageLocation.fileName)

            if (! matchingPages.length) {
                console.error("can't find any page with", currentPageLocation)
                return
            }

            const tocItem = matchingPages[0].tocItem
            this.changePage({page: matchingPages[0], selectedTocItem: currentPageLocation, lastChangeDataDom: null})
            this.scrollToTop()

            this.scrollToPageSection(tocItem, currentPageLocation.pageSectionId)
            return true
        }, (error) => console.error(error))
    }

    getAllPagesPromise() {
        const {docMeta} = this.props
        return getAllPagesPromise(docMeta)
    }

    extractPageSectionNodes() {
        this.pageSectionNodes = [...document.querySelectorAll(".section-title")]
    }

    scrollToPageSection(tocItem, pageSectionId) {
        const sectionIdx = tocItem.pageSectionIdTitles.map(ps => ps.id).indexOf(pageSectionId);
        if (sectionIdx >= 0) {
            this.pageSectionNodes[sectionIdx].scrollIntoView();
        }
    }

    updateCurrentPageSection() {
        const {isPresentationMode, page, selectedTocItem} = this.state

        if (isPresentationMode) {
            return
        }

        const pageSections = page.tocItem.pageSectionIdTitles
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

        const enrichedSelectedTocItem = {...selectedTocItem, pageSectionId: (current && current.idTitle) ? current.idTitle.id : null}
        this.setState({selectedTocItem: enrichedSelectedTocItem})
    }
}

export default Documentation
