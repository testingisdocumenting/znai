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

import React, {Component} from 'react'
import * as Promise from 'promise'

import {themeRegistry} from '../theme/ThemeRegistry'
import WithTheme from '../theme/WithTheme'

import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import {documentationNavigation} from './structure/DocumentationNavigation'
import {documentationTracking} from './tracking/DocumentationTracking'
import {textSelection} from './selected-text-extensions/TextSelection'
import {tableOfContents} from './structure/toc/TableOfContents'
import {getAllPagesPromise} from './allPages'
import {fullResourcePath} from '../utils/resourcePath'

import Presentation from './presentation/Presentation'
import Preview from './preview/Preview'
import PreviewChangeIndicator from './preview/PreviewChangeIndicator'
import PageContentPreviewDiff from './preview/PageContentPreviewDiff'

import PresentationRegistry from './presentation/PresentationRegistry'

import AllPagesAtOnce from './AllPagesAtOnce'

import {mergeDocMeta} from './docMeta'

import pageContentProcessor from './pageContentProcessor.js'

import {DocumentationModes} from './DocumentationModes'
import {pageTypesRegistry} from './page/PageTypesRegistry'

import {injectGlobalOverridesCssLink} from './CssOverrides'

import {updateGlobalDocReferences} from './references/globalDocReferences'

import './DocumentationLayout.css'
import './search/Search.css'

export class Documentation extends Component {
    constructor(props) {
        super(props)

        const {page, docMeta} = this.props

        mergeDocMeta(docMeta)
        this.searchPromise = getSearchPromise(docMeta)

        const autoSelectedTocItem = {
            dirName: page.tocItem.dirName,
            fileName: page.tocItem.fileName,
            anchorId: page.tocItem.pageSectionIdTitles[0] ?
                page.tocItem.pageSectionIdTitles[0].id : null
        }

        this.state = {
            tocCollapsed: false,
            tocSelected: false,
            page: Documentation.processPage(page),
            toc: tableOfContents.toc,
            docMeta: docMeta,
            forceSelectedTocItem: null, // via explicit TOC panel click
            autoSelectedTocItem: autoSelectedTocItem, // based on scrolling
            mode: DocumentationModes.DEFAULT
        }

        this.onHeaderClick = this.onHeaderClick.bind(this)
        this.onPresentationOpen = this.onPresentationOpen.bind(this)
        this.onPresentationClose = this.onPresentationClose.bind(this)
        this.onTocToggle = this.onTocToggle.bind(this)
        this.onTocSelect = this.onTocSelect.bind(this)
        this.onTocItemClick = this.onTocItemClick.bind(this)
        this.onTocItemPageSectionClick = this.onTocItemPageSectionClick.bind(this)
        this.onSearchClick = this.onSearchClick.bind(this)
        this.onSearchClose = this.onSearchClose.bind(this)
        this.onPanelSelect = this.onPanelSelect.bind(this)
        this.onNextPage = this.onNextPage.bind(this)
        this.onPrevPage = this.onPrevPage.bind(this)
        this.onSearchSelection = this.onSearchSelection.bind(this)
        this.onPageUpdate = this.onPageUpdate.bind(this)
        this.onTocUpdate = this.onTocUpdate.bind(this)
        this.onDocMetaUpdate = this.onDocMetaUpdate.bind(this)
        this.onMultiplePagesUpdate = this.onMultiplePagesUpdate.bind(this)
        this.onDocReferencesUpdate = this.onDocReferencesUpdate.bind(this)
        this.onPageGenError = this.onPageGenError.bind(this)
        this.updateCurrentPageSection = this.updateCurrentPageSection.bind(this)
        this.keyDownHandler = this.keyDownHandler.bind(this)
        this.mouseUpHandler = this.mouseUpHandler.bind(this)
        this.mouseClickHandler = this.mouseClickHandler.bind(this)

        documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
        textSelection.addListener(this.onTextSelection.bind(this))
    }

    get theme() {
        return themeRegistry.currentTheme
    }

    render() {
        const {mode} = this.state

        switch (mode) {
            case DocumentationModes.DEFAULT:
                return this.renderDefaultDocMode()
            case DocumentationModes.PRESENTATION:
                return this.renderPresentationMode()
            case DocumentationModes.PRINT:
                return this.renderPrintMode()
            default:
                return <div>No handler for documentation mode: {mode}</div>
        }
    }

    renderDefaultDocMode() {
        const {footer} = this.props
        const {
            toc,
            page,
            docMeta,
            autoSelectedTocItem,
            forceSelectedTocItem,
            tocCollapsed,
            lastChangeDataDom,
            isSearchActive,
            textSelection,
            pageGenError,
        } = this.state

        const theme = this.theme
        const elementsLibrary = theme.elementsLibrary

        const searchPopup = isSearchActive ? <SearchPopup elementsLibrary={elementsLibrary}
                                                          tocCollapsed={tocCollapsed}
                                                          searchPromise={this.searchPromise}
                                                          onSearchSelection={this.onSearchSelection}
                                                          onClose={this.onSearchClose}/> : null

        const renderedPage = <elementsLibrary.Page {...page}
                                                   docMeta={docMeta}
                                                   onPresentationOpen={this.onPresentationOpen}
                                                   prevPageTocItem={this.prevPageTocItem}
                                                   nextPageTocItem={this.nextPageTocItem}
                                                   onNextPage={this.onNextPage}
                                                   onPrevPage={this.onPrevPage}
                                                   previewEnabled={docMeta.previewEnabled}
                                                   elementsLibrary={elementsLibrary}/>

        const NextPrevNavigation = pageTypesRegistry.nextPrevNavigationComponent(page.tocItem)
        const renderedNextPrevNavigation = <NextPrevNavigation prevPageTocItem={this.prevPageTocItem}
                                                               nextPageTocItem={this.nextPageTocItem}
                                                               onNextPage={this.onNextPage}
                                                               onPrevPage={this.onPrevPage}/>

        const renderedFooter = (footer && Object.keys(footer).length) ?
            <elementsLibrary.Footer {...footer} elementsLibrary={elementsLibrary}/> : null

        const preview = docMeta.previewEnabled ? <Preview active={true}
                                                          onPageUpdate={this.onPageUpdate}
                                                          onMultiplePagesUpdate={this.onMultiplePagesUpdate}
                                                          onDocReferencesUpdate={this.onDocReferencesUpdate}
                                                          onTocUpdate={this.onTocUpdate}
                                                          onDocMetaUpdate={this.onDocMetaUpdate}
                                                          onError={this.onPageGenError}/> : null

        const DocumentationLayout = elementsLibrary.DocumentationLayout
        const selectedTocItem = {...page.tocItem, ...(forceSelectedTocItem || autoSelectedTocItem)}

        return (
            <WithTheme>{() =>
                <React.Fragment>
                    <DocumentationLayout docMeta={docMeta}
                                         toc={toc}
                                         theme={theme}
                                         selectedTocItem={selectedTocItem}
                                         prevPageTocItem={this.prevPageTocItem}
                                         nextPageTocItem={this.nextPageTocItem}
                                         searchPopup={searchPopup}
                                         renderedPage={renderedPage}
                                         renderedNextPrevNavigation={renderedNextPrevNavigation}
                                         renderedFooter={renderedFooter}
                                         onHeaderClick={this.onHeaderClick}
                                         onSearchClick={this.onSearchClick}
                                         onTocItemClick={this.onTocItemClick}
                                         onTocItemPageSectionClick={this.onTocItemPageSectionClick}
                                         onNextPage={this.onNextPage}
                                         onPrevPage={this.onPrevPage}
                                         textSelection={textSelection}
                                         pageGenError={pageGenError}/>
                    {preview}
                    {lastChangeDataDom && <PreviewChangeIndicator targetDom={lastChangeDataDom}
                                                                  onIndicatorRemove={this.onPreviewIndicatorRemove}/>}
                </React.Fragment>
            }
            </WithTheme>
        )
    }

    renderPresentationMode() {
        const {presentationRegistry, docMeta} = this.state

        return (
            <WithTheme>{() =>
                <Presentation docMeta={docMeta}
                              presentationRegistry={presentationRegistry}
                              onClose={this.onPresentationClose}
                              onNextPage={this.onNextPage}
                              hasNextPage={this.hasNextPage()}
                              onPrevPage={this.onPrevPage}/>
            }
            </WithTheme>
        )
    }

    renderPrintMode() {
        const {docMeta} = this.state

        return <AllPagesAtOnce docMeta={docMeta}/>
    }

    componentDidMount() {
        injectGlobalOverridesCssLink()

        this.enableScrollListener()
        this.onPageLoad()

        document.addEventListener('keydown', this.keyDownHandler)
        document.addEventListener('mouseup', this.mouseUpHandler)
        document.addEventListener('click', this.mouseClickHandler)
    }

    componentWillUnmount() {
        this.disableScrollListener()

        document.removeEventListener('keydown', this.keyDownHandler)
        document.removeEventListener('mouseup', this.mouseUpHandler)
        document.removeEventListener('click', this.mouseClickHandler)
    }

    keyDownHandler(e) {
        const {isSearchActive, mode} = this.state
        if (e.code === "Slash" && !isSearchActive && mode === DocumentationModes.DEFAULT) {
            e.preventDefault()
            this.setState({isSearchActive: true})
        } else if (!isSearchActive && mode === DocumentationModes.DEFAULT && e.code === 'KeyP' && !e.ctrlKey && !e.altKey) {
            this.onPresentationOpen()
        } else if (mode === DocumentationModes.DEFAULT && e.code === 'KeyP' && e.altKey) {
            this.setState({mode: DocumentationModes.PRINT})
        } else if (mode === DocumentationModes.DEFAULT && e.code === 'ArrowLeft' && e.ctrlKey) {
            this.onPrevPage()
        } else if (mode === DocumentationModes.DEFAULT && e.code === 'ArrowRight' && e.ctrlKey) {
            this.onNextPage()
        } else if (e.code === "Escape") {
            this.onPresentationClose()
        }
    }

    mouseUpHandler() {
        const {isSearchActive, mode} = this.state
        if (mode !== DocumentationModes.DEFAULT || isSearchActive) {
            return
        }

        const selection = window.getSelection()
        if (!selection.rangeCount) {
            return
        }

        const rangeAt = selection.getRangeAt(0)
        const text = selection.toString()

        if (!text || selection.isCollapsed) {
            textSelection.clear()
        } else {
            const {page} = this.state

            const tocItem = page.tocItem
            const startNode = selection.isCollapsed ? null : rangeAt.startContainer.parentNode

            textSelection.endSelection({tocItem, startNode, text})
        }
    }

    mouseClickHandler() {
    }

    saveMainPanelDomRef() {
        this.mainPanelDom = document.querySelector(".main-panel")
    }

    enableScrollListener() {
        // server side rendering guard
        if (window.addEventListener) {
            this.saveMainPanelDomRef()
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
        this.setState({
            ...newStateWithNewPage,
            page: Documentation.processPage(newStateWithNewPage.page),
            pageGenError: null
        }, () => this.onPageLoad())
    }

    static processPage(page) {
        return {...page, content: pageContentProcessor.process(page.content)}
    }

    scrollToTop() {
        this.mainPanelDom.scrollTop = 0
    }

    onPageLoad() {
        const {page, docMeta} = this.state

        this.extractPageSectionNodes()

        const currentPageLocation = documentationNavigation.currentPageLocation()
        documentationTracking.onPageOpen(currentPageLocation)

        const anchorId = currentPageLocation.anchorId
        if (anchorId) {
            this.scrollToPageSection(anchorId)
        } else {
            this.scrollToTop()
        }

        this.updateCurrentPageSection()

        const theme = this.theme
        const presentationRegistry = new PresentationRegistry(theme.elementsLibrary, theme.presentationElementHandlers, page)

        const isIndex = tableOfContents.isIndex(page.tocItem)
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

    get nextPageTocItem() {
        const {page} = this.state
        return tableOfContents.nextTocItem(page.tocItem)
    }

    get prevPageTocItem() {
        const {page} = this.state
        return tableOfContents.prevTocItem(page.tocItem)
    }

    hasNextPage() {
        return this.nextPageTocItem !== null
    }

    onNextPage() {
        const next = this.nextPageTocItem
        if (next) {
            documentationTracking.onNextPage()
            documentationNavigation.navigateToPage(next)
        }
    }

    onPrevPage() {
        const prev = this.prevPageTocItem
        if (prev) {
            documentationTracking.onPrevPage()
            documentationNavigation.navigateToPage(prev)
        }
    }

    onHeaderClick() {
        const url = fullResourcePath(this.state.docMeta.id, "")
        documentationNavigation.navigateToUrl(url)
    }

    onPresentationOpen() {
        documentationTracking.onPresentationOpen()
        this.setState({mode: DocumentationModes.PRESENTATION})
    }

    onPresentationClose() {
        this.switchToDefaultMode()
    }

    switchToDefaultMode() {
        this.setState({mode: DocumentationModes.DEFAULT}, () => {
            this.saveMainPanelDomRef()
            this.extractPageSectionNodes()
            this.updateCurrentPageSection()
            this.enableScrollListener()
        })
    }

    onTocItemClick(dirName, fileName) {
        documentationTracking.onTocItemSelect({dirName, fileName, anchorId: ''})
        documentationNavigation.navigateToPage({dirName, fileName})
    }

    onTocItemPageSectionClick(sectionId) {
        const {autoSelectedTocItem} = this.state

        const forceSelectedTocItem = {...autoSelectedTocItem, anchorId: sectionId}

        documentationTracking.onTocItemSelect(forceSelectedTocItem)
        this.setState({forceSelectedTocItem})
    }

    onSearchSelection(query, id) {
        this.onSearchClose()
        documentationTracking.onSearchResultSelect(query, id)
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
        if (!tableOfContents.hasTocItem(this.state.page.tocItem)) {
            documentationNavigation.navigateToPage(tableOfContents.first)
        }
    }

    onDocMetaUpdate(docMeta) {
        this.setState({docMeta})
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

    onDocReferencesUpdate(docReferences) {
        this.updatePageAndDetectChangePosition(() => {
            updateGlobalDocReferences(docReferences)
            this.changePage({page: this.state.page})
        })
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
        }).then(() => {
        }, (error) => console.error(error))
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
            foundPages.forEach((page) => {
                page.content = newPage.content
            })
        } else {
            allPages.push(newPage)
        }
    }

    onTextSelection(textSelection) {
        this.setState({textSelection})
    }

    onUrlChange(url) {
        return this.getAllPagesPromise().then((pages) => {
            const currentPageLocation = documentationNavigation.extractPageLocation(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === currentPageLocation.dirName &&
                p.tocItem.fileName === currentPageLocation.fileName)

            if (!matchingPages.length) {
                console.error("can't find any page with", currentPageLocation)
                return
            }

            this.changePage({
                page: matchingPages[0],
                forceSelectedTocItem: currentPageLocation,
                autoSelectedTocItem: currentPageLocation,
                lastChangeDataDom: null
            })
            return true
        }, (error) => console.error(error))
    }

    getAllPagesPromise() {
        const {docMeta} = this.state
        return getAllPagesPromise(docMeta)
    }

    extractPageSectionNodes() {
        this.pageSectionNodes = [...document.querySelectorAll(".section-title")]
    }

    scrollToPageSection(pageSectionId) {
        documentationNavigation.scrollToAnchor(pageSectionId)
    }

    updateCurrentPageSection() {
        const {mode, page, autoSelectedTocItem, forceSelectedTocItem} = this.state

        if (mode !== DocumentationModes.DEFAULT) {
            return
        }

        const sectionTitlesWithNode = combineSectionTitlesWithNodes(this.pageSectionNodes)
        const withVisibleTitle = sectionsWithVisibleTitle()

        if (forceSelectedTocItem && isVisible(forceSelectedTocItem.anchorId)) {
            return;
        }

        const visible = withVisibleTitle.length ? withVisibleTitle[0] : closestToTopZero()

        const enrichedSelectedTocItem = {
            ...autoSelectedTocItem,
            anchorId: (visible && visible.idTitle) ? visible.idTitle.id : null
        }
        this.setState({autoSelectedTocItem: enrichedSelectedTocItem, forceSelectedTocItem: null})

        if (sectionTitlesWithNode.length !== 0 &&
            autoSelectedTocItem.anchorId !== visible.idTitle.id) {
            documentationTracking.onScrollToSection(visible.idTitle)
        }

        function combineSectionTitlesWithNodes(pageSectionNodes) {
            const pageSections = page.tocItem.pageSectionIdTitles

            return pageSectionNodes
                .filter(isNodeIdPresentInSections)
                .map((n, idx) => {
                    return {idTitle: pageSections[idx], rect: n.getBoundingClientRect()}
                })

            // case where znai page has an example of rendered markdown
            // it generates extra nodes matching section-title css, but that node is not part
            // of table of contents, so needs to be excluded
            function isNodeIdPresentInSections(node) {
                return pageSections.filter(s => s.id === node.id).length > 0
            }
        }

        function sectionsWithVisibleTitle() {
            const height = window.innerHeight
            return (sectionTitlesWithNode).filter(st => {
                const rect = st.rect
                return rect.top > -10 && rect.top < height
            })
        }

        function isVisible(id) {
            const visibleWithForcedId =
                withVisibleTitle.filter(s => s.idTitle.id === id)

            return visibleWithForcedId.length > 0
        }

        function closestToTopZero() {
            const belowZero = sectionTitlesWithNode.filter(st => st.rect.top < 0)
            return belowZero.length ? belowZero[belowZero.length - 1] : sectionTitlesWithNode[0]
        }
    }

    onPreviewIndicatorRemove = () => {
        this.setState({lastChangeDataDom: null})
    }
}
