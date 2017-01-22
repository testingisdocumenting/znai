import React, { Component } from "react"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import elementsLibrary from './DefaultElementsLibrary'
import {documentationNavigation} from './DocumentationNavigation'
import {getAllPagesPromise} from "./allPages"

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

        documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
    }

    render() {
        const {toc, docMeta} = this.props
        const {page, tocCollapsed, tocSelected} = this.state

        const pageTitle = page.tocItem.pageTitle

        const searchPopup = this.state.searchActive ? <SearchPopup searchPromise={this.searchPromise}
                                                                   onSearchSelection={this.onSearchSelection}
                                                                   onClose={this.onSearchClose}/> : null

        return (
            <div className="documentation">
                <div className="side-panel" onClick={this.onTocSelect}>
                    <TocPanel toc={toc} collapsed={tocCollapsed} selected={tocSelected}
                              onToggle={this.onTocToggle}
                              selectedItem={this.state.selectedTocItem}
                              onNextPage={this.onNextPage}
                              onPrevPage={this.onPrevPage}/>
                </div>

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

    onUrlChange(url) {
        getAllPagesPromise().then((pages) => {
            const currentPageLocation = documentationNavigation.extractDirNameAndFileName(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === currentPageLocation.dirName &&
                p.tocItem.fileName === currentPageLocation.fileName)

            if (! matchingPages) {
                console.error("can't find any page with", currentPageLocation)
                return
            }

            this.setState({page: matchingPages[0], selectedTocItem: currentPageLocation})
        })
    }
}

export default Documentation
